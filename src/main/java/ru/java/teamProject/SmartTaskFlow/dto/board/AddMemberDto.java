package ru.java.teamProject.SmartTaskFlow.dto.board;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AddMemberDto {

    @NotBlank
    private String usernameOrEmail;
}
