package ru.java.teamProject.SmartTaskFlow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.java.teamProject.SmartTaskFlow.dto.user.RegisterUserDTO;
import ru.java.teamProject.SmartTaskFlow.dto.user.UpdateProfileDTO;
import ru.java.teamProject.SmartTaskFlow.dto.user.UserResponseDTO;
import ru.java.teamProject.SmartTaskFlow.entity.User;
import ru.java.teamProject.SmartTaskFlow.jwt.JwtUtils;
import ru.java.teamProject.SmartTaskFlow.repository.UserRepository;
import ru.java.teamProject.SmartTaskFlow.service.abstr.UserService;

import java.util.Collections;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
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
        userRepository.save(user);
    }

    public String authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email).
                orElseThrow(() -> new RuntimeException("Invalid email or password"));
        if (passwordEncoder.matches(password, user.getPassword())) {
            return jwtUtils.generateToken(email);
        }
        throw new RuntimeException("Invalid email or password");
    }

    public void updateProfile(String email, UpdateProfileDTO profileDTO) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Optional.ofNullable(profileDTO.getFirstName()).ifPresent(user::setFirstName);
        Optional.ofNullable(profileDTO.getLastName()).ifPresent(user::setLastName);
        Optional.ofNullable(profileDTO.getUsername()).ifPresent(user::setUsername);

        if (profileDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(profileDTO.getPassword()));
        }
        userRepository.save(user);
    }

    public void logout(String email) {
        // По хорошему тут нужен jwt
    }

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
