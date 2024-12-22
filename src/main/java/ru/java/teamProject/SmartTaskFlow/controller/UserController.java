package ru.java.teamProject.SmartTaskFlow.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.java.teamProject.SmartTaskFlow.dto.user.LoginDTO;
import ru.java.teamProject.SmartTaskFlow.dto.user.RegisterUserDTO;
import ru.java.teamProject.SmartTaskFlow.dto.user.UpdateProfileDTO;
import ru.java.teamProject.SmartTaskFlow.service.abstr.UserService;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterUserDTO registerDTO, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid data provided.");
        }
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Passwords do not match.");
        }
        userService.registerUser(registerDTO);
        return ResponseEntity.ok("User registered successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginDTO loginDTO) {
        boolean authenticated = userService.authenticateUser(loginDTO.getEmail(), loginDTO.getPassword());
        if (!authenticated) {
            return ResponseEntity.status(401).body("Invalid email or password.");
        }
        return ResponseEntity.ok("Login successful.");
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(Authentication authentication, @Valid @RequestBody UpdateProfileDTO profileDTO) {
        String email = authentication.getName();
        userService.updateProfile(email, profileDTO);
        return ResponseEntity.ok("Profile updated successfully.");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(Authentication authentication) {
        userService.logout(authentication.getName());
        return ResponseEntity.ok("Logout successful.");
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully.");
    }
}
