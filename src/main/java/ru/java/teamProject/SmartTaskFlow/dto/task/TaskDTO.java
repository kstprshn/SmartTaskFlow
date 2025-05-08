package ru.java.teamProject.SmartTaskFlow.dto.task;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Setter @Getter
@Accessors(chain = true)
public class TaskDTO {
    private Long id;
    private String name;
    private String description;
    private Integer orderIndex;
    private boolean archived;

    private String startTime;
    private String endTime;

    private List<Long> subTaskIds = new ArrayList<>();

    private Long panelId;
}