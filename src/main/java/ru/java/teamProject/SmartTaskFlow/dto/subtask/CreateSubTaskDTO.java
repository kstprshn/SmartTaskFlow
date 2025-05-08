package ru.java.teamProject.SmartTaskFlow.dto.subtask;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateSubTaskDTO {

    @NotBlank
    private String name;

    @NotNull
    private Long taskId;

}
