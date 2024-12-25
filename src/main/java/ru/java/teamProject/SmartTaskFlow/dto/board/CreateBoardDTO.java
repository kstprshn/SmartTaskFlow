package ru.java.teamProject.SmartTaskFlow.dto.board;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateBoardDTO {
    @NotNull
    @Size(min = 3, max = 30)
    private String name;

    @NotEmpty
    private String description;
}