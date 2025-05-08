package ru.java.teamProject.SmartTaskFlow.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.java.teamProject.SmartTaskFlow.service.abstr.UserService;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserService userServiceImpl;

    private final RedisTokenBlacklist redisTokenBlacklist;

    @Autowired
    public JwtAuthenticationFilter(JwtUtils jwtUtils, UserService userServiceImpl,
                                                    RedisTokenBlacklist redisTokenBlacklist) {
        this.jwtUtils = jwtUtils;
        this.userServiceImpl = userServiceImpl;
        this.redisTokenBlacklist = redisTokenBlacklist;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {



        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            log.info("Received token: {}", token);

            if (token.isBlank()) {

                log.warn("Received blank JWT token");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT token");
                return;
            }

            boolean valid = jwtUtils.validateToken(token);
            boolean blacklisted = redisTokenBlacklist.isBlacklisted(token);
            log.info("Token validation result: valid={}, blacklisted={}", valid, blacklisted);

            if (valid && !blacklisted) {
                String username = jwtUtils.getUsernameFromToken(token);
                UserDetails userDetails = userServiceImpl.loadUserByUsername(username);

                log.info("Setting authentication for user: {}", username);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                log.warn("JWT rejected. Valid: {}, Blacklisted: {}", valid, blacklisted);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is invalid or blacklisted");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
