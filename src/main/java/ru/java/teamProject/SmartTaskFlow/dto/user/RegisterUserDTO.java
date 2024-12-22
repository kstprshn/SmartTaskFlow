package ru.java.teamProject.SmartTaskFlow.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegisterUserDTO {

    private String firstName;
    private String lastName;
    private String username;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 8, max = 20)
    private String password;

    @NotNull
    private String confirmPassword;
}