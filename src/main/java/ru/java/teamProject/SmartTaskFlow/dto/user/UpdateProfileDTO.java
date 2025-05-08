package ru.java.teamProject.SmartTaskFlow.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class UpdateProfileDTO {

    private String firstName;
    private String lastName;
    private String username;


    @Email
    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@(gmail\\.com|mail\\.ru|edu\\.misis\\.ru|yandex\\.ru)$",
            message = "Email must be in one of the supported formats: gmail.com, mail.ru, edu.misis.ru, yandex.ru"
    )
    private String password;
}