package ru.java.teamProject.SmartTaskFlow.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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

    @NotBlank(message = "Email cannot be empty")
    @Email
    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@(gmail\\.com|mail\\.ru|edu\\.misis\\.ru|yandex\\.ru)$",
            message = "Email must be in one of the supported formats: gmail.com, mail.ru, edu.misis.ru, yandex.ru"
    )
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, max = 20)
    private String password;

    @NotBlank(message = "Please, confirm your password!")
    private String confirmPassword;
}