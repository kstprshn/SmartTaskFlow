package ru.java.teamProject.SmartTaskFlow.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class UserResponseDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String username;
}

