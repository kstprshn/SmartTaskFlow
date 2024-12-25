package ru.java.teamProject.SmartTaskFlow.dto.task;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.java.teamProject.SmartTaskFlow.entity.enums.Priority;

@Setter @Getter
@Accessors(chain = true)
public class TaskDTO {
    private String name;
    private Priority priority;
    private String description;
    private Integer orderIndex;
    private boolean archived;

    private String startTime;
    private String endTime;

    private Long panelId;
}