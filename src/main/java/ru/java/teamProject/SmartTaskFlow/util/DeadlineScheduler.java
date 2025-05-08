package ru.java.teamProject.SmartTaskFlow.util;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.java.teamProject.SmartTaskFlow.dto.user.DeadlineApproachingEvent;
import ru.java.teamProject.SmartTaskFlow.entity.Task;
import ru.java.teamProject.SmartTaskFlow.entity.User;
import ru.java.teamProject.SmartTaskFlow.producer.TaskEventProducer;
import ru.java.teamProject.SmartTaskFlow.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DeadlineScheduler {

    private final TaskRepository taskRepository;
    private final TaskEventProducer taskEventProducer;

    @Scheduled(fixedRate = 60 * 60 * 1000) // 1 раз в час
    public void sendDeadlineNotifications() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime next24h = now.plusHours(24);
        List<Task> tasks = taskRepository.findByEndDateBetween(now, next24h);  //сутки до окончания

        for (Task task : tasks) {
            List<String> emails = task.getAssignees().stream()
                    .map(User::getEmail).toList();

            taskEventProducer.sendDeadlineEvent(new DeadlineApproachingEvent(
                    task.getId(),
                    task.getName(),
                    task.getEndDate(),
                    emails
            ));
        }
    }
}
