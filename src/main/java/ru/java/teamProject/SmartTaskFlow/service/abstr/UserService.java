package ru.java.teamProject.SmartTaskFlow.service.abstr;

import ru.java.teamProject.SmartTaskFlow.dto.user.RegisterUserDTO;
import ru.java.teamProject.SmartTaskFlow.dto.user.UpdateProfileDTO;
import ru.java.teamProject.SmartTaskFlow.dto.user.UserResponseDTO;

public interface UserService {
    void registerUser(RegisterUserDTO registerDTO);
    String authenticateUser(String email, String password);
    void updateProfile(String email, UpdateProfileDTO profileDTO);
    void logout(String email);
    void deleteUser(Long userId);

    UserResponseDTO getUserById(Long id);
    UserResponseDTO getUserByEmailOrUsername(String identifier);

}
