package ru.java.teamProject.SmartTaskFlow.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.java.teamProject.SmartTaskFlow.dto.user.RegisterUserDTO;
import ru.java.teamProject.SmartTaskFlow.dto.user.UpdateProfileDTO;
import ru.java.teamProject.SmartTaskFlow.dto.user.UserResponseDTO;
import ru.java.teamProject.SmartTaskFlow.entity.User;
import ru.java.teamProject.SmartTaskFlow.security.jwt.JwtUtils;
import ru.java.teamProject.SmartTaskFlow.repository.UserRepository;
import ru.java.teamProject.SmartTaskFlow.security.jwt.RedisTokenBlacklist;
import ru.java.teamProject.SmartTaskFlow.service.abstr.UserService;

import java.util.Collections;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    private final RedisTokenBlacklist redisTokenBlacklist;
    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();


    private UserResponseDTO buildUserResponse(User user) {
        return new UserResponseDTO()
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setEmail(user.getEmail())
                .setUsername(user.getUsername());
    }
    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return buildUserResponse(user);
    }

    @Override
    public UserResponseDTO getUserByEmailOrUsername(String identifier) {
        User user = userRepository.findByUsernameOrEmailIgnoreCase(identifier, identifier)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return buildUserResponse(user);
    }

    @Override
    public void registerUser(RegisterUserDTO registerDTO) {
        if (userRepository.existsByEmailIgnoreCase(registerDTO.getEmail())) {
            throw new RuntimeException("Email is already in use");
        }
        User user = new User();
        user.setEmail(registerDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setFirstName(registerDTO.getFirstName());
        user.setLastName(registerDTO.getLastName());
        user.setUsername(registerDTO.getUsername());

        log.info("Adding a new user");
        userRepository.save(user);
    }

    @Override
    public void registerOAuthUser(String email, String firstName, String lastName) {
        if (userRepository.existsByEmailIgnoreCase(email)) {
            return;         // Пользователь уже существует -> просто генерация jwt-токена
                            // если нет - создать нового юзера (без пароля)
        }
        User user = new User();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(email);
        user.setPassword("");

        log.info("Registering new OAuth user: {}", email);
        userRepository.save(user);
    }


    public String authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email).
                orElseThrow(() -> new RuntimeException("Invalid email or password"));
        if (passwordEncoder.matches(password, user.getPassword())) {
            log.info("User with the mail {} log in to the system", user.getEmail());
            return jwtUtils.generateToken(email);
        }
        throw new RuntimeException("Invalid email or password");
    }

    @Override
    public void updateProfile(Authentication authentication, UpdateProfileDTO profileDTO) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow(
                                                () -> new RuntimeException("User not found"));

        Optional.ofNullable(profileDTO.getFirstName()).ifPresent(user::setFirstName);
        Optional.ofNullable(profileDTO.getLastName()).ifPresent(user::setLastName);
        Optional.ofNullable(profileDTO.getUsername()).ifPresent(user::setUsername);

        if (profileDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(profileDTO.getPassword()));
        }
        userRepository.save(user);
    }

    @Override
    public void logout(String token) {
        if (jwtUtils.validateToken(token)) {
            long expiration = jwtUtils.getExpiration(token);
            redisTokenBlacklist.blacklistToken(token, expiration);
            log.info("JWT has been moved to the black list in Redis: {}", token);
        } else {
            throw new RuntimeException("Invalid token");
        }
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public UserResponseDTO getUser(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("User not found"));

        return buildUserResponse(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.emptyList()
        );
    }
}
