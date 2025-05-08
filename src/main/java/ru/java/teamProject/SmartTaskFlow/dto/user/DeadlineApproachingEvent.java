package ru.java.teamProject.SmartTaskFlow.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeadlineApproachingEvent {
    private Long taskId;
    private String taskName;
    private LocalDateTime deadline;
    private List<String> recipientEmails;
}
