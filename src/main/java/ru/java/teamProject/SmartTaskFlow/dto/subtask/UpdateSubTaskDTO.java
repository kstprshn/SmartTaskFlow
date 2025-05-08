package ru.java.teamProject.SmartTaskFlow.dto.subtask;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateSubTaskDTO {
    private String name;
    private boolean completed;
}
