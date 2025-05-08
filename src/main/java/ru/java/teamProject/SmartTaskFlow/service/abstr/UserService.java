package ru.java.teamProject.SmartTaskFlow.service.abstr;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.java.teamProject.SmartTaskFlow.dto.user.RegisterUserDTO;
import ru.java.teamProject.SmartTaskFlow.dto.user.UpdateProfileDTO;
import ru.java.teamProject.SmartTaskFlow.dto.user.UserResponseDTO;

public interface UserService extends UserDetailsService {
    void registerUser(RegisterUserDTO registerDTO);
    void registerOAuthUser(String email, String firstName, String lastName);
    String authenticateUser(String email, String password);
    void updateProfile(Authentication authentication, UpdateProfileDTO profileDTO);
    void logout(String token);
    void deleteUser(Long userId);
    UserResponseDTO getUser(Authentication authentication);
    UserResponseDTO getUserById(Long id);
    UserResponseDTO getUserByEmailOrUsername(String identifier);

}
