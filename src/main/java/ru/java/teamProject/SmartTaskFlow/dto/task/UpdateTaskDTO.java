package ru.java.teamProject.SmartTaskFlow.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Getter;
import lombok.Setter;
import ru.java.teamProject.SmartTaskFlow.entity.enums.Priority;

import java.time.LocalDateTime;

@Getter @Setter
public class UpdateTaskDTO {
    private String name;
    private Priority priority;
    private Integer orderIndex;
    private String description;

    @FutureOrPresent(message = "Start date must be in the present or future")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    @Future(message = "End date must be in the future")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
}