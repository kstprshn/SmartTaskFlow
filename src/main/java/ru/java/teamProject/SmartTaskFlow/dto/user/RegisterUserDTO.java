package ru.java.teamProject.SmartTaskFlow.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegisterUserDTO {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String username;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 8, max = 20)
    private String password;
    @NotBlank
    private String confirmPassword;
}