package ru.java.teamProject.SmartTaskFlow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.java.teamProject.SmartTaskFlow.dto.user.RegisterUserDTO;
import ru.java.teamProject.SmartTaskFlow.dto.user.UpdateProfileDTO;
import ru.java.teamProject.SmartTaskFlow.entity.User;
import ru.java.teamProject.SmartTaskFlow.entity.enums.RoleType;
import ru.java.teamProject.SmartTaskFlow.repository.UserRepository;
import ru.java.teamProject.SmartTaskFlow.service.abstr.UserService;


@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public void registerUser(RegisterUserDTO registerDTO) {
        User user = new User();
        user.setEmail(registerDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setFirstName(registerDTO.getFirstName());
        user.setLastName(registerDTO.getLastName());
        user.setUsername(registerDTO.getUsername());
        userRepository.save(user);
    }

    public boolean authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email).orElse(null);
        return user != null && passwordEncoder.matches(password, user.getPassword());
    }

    public void updateProfile(String email, UpdateProfileDTO profileDTO) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        user.setFirstName(profileDTO.getFirstName());
        user.setLastName(profileDTO.getLastName());
        user.setUsername(profileDTO.getUsername());
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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getRoles());
    }
}
