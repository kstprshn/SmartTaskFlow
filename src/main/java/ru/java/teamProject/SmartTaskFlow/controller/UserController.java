package ru.java.teamProject.SmartTaskFlow.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.java.teamProject.SmartTaskFlow.dto.user.LoginDTO;
import ru.java.teamProject.SmartTaskFlow.dto.user.RegisterUserDTO;
import ru.java.teamProject.SmartTaskFlow.dto.user.UpdateProfileDTO;
import ru.java.teamProject.SmartTaskFlow.dto.user.UserResponseDTO;
import ru.java.teamProject.SmartTaskFlow.service.abstr.UserService;

import java.util.Collections;


@RestController
@RequestMapping("/api/users")
@Tag(name = "User Api", description = "Methods for working with User API")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}/getUser")
    @Operation(summary = "Receiving the user")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/search")
    @Operation(summary = "Searching the user")
    public ResponseEntity<UserResponseDTO> getUserByEmailOrUsername(@Valid @RequestBody String identifier) {
        return ResponseEntity.ok(userService.getUserByEmailOrUsername(identifier));
    }
    @PostMapping("/register")
    @Operation(summary = "Registering a new user")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterUserDTO registerDTO, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid data provided.");
        }
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Passwords do not match.");
        }
        userService.registerUser(registerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully.");
    }

    @PostMapping("/login")
    @Operation(summary = "Log in to the system")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            String token = userService.authenticateUser(loginDTO.getEmail(), loginDTO.getPassword());
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/profile")
    @Operation(summary = "Receiving user's profile")
    public ResponseEntity<UserResponseDTO> getUserProfile(Authentication authentication) {
        return ResponseEntity.ok(userService.getUser(authentication));
    }

    @PutMapping("/profile/edit")
    @Operation(summary = "Editing the user's profile")
    public ResponseEntity<String> updateProfile(Authentication authentication, @Valid @RequestBody UpdateProfileDTO profileDTO) {
        userService.updateProfile(authentication, profileDTO);
        return ResponseEntity.status(HttpStatus.OK).body("Profile updated successfully.");
    }

    @PostMapping("/logout")
    @Operation(summary = "Log out of the system")
    public ResponseEntity<String> logoutUser(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        userService.logout(token);
        return ResponseEntity.ok("Logout successful.");
    }


    @DeleteMapping("/{userId}/remove")
    @Operation(summary = "Deleting the user")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted successfully.");
    }
}
