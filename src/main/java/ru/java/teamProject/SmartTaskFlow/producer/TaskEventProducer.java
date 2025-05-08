package ru.java.teamProject.SmartTaskFlow.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.java.teamProject.SmartTaskFlow.dto.user.DeadlineApproachingEvent;
import ru.java.teamProject.SmartTaskFlow.dto.user.UserAssignedToTaskEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendUserAssignedEvent(UserAssignedToTaskEvent event) {
        kafkaTemplate.send("user-notifications", event);
        log.info("User with email: {} assigned to the task: {}", event.getAssignedUserEmail(),event.getTaskName());
    }

    public void sendDeadlineEvent(DeadlineApproachingEvent event) {
        kafkaTemplate.send("task-deadlines", event);
        log.info("Task name is: {},\n User email is: {}, deadline is: {}", event.getTaskName(), event.getRecipientEmails(), event.getDeadline());
    }
}
