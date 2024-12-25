package ru.java.teamProject.SmartTaskFlow.dto.subtask;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class SubTaskDTO {
    Long id;
    String name;
    Long taskId;
}
