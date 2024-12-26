package ru.java.teamProject.SmartTaskFlow.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter @Getter
public class CreateTaskDTO {

    @NotNull
    private String name;
    private Integer orderIndex;
    private String description;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @FutureOrPresent(message = "Start date must be in the present or future")
    private LocalDateTime startDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Future(message = "End date must be in the future")
    private LocalDateTime endDate;
}