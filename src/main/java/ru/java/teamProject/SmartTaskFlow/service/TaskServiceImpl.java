package ru.java.teamProject.SmartTaskFlow.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.java.teamProject.SmartTaskFlow.dto.task.CreateTaskDTO;
import ru.java.teamProject.SmartTaskFlow.dto.task.TaskDTO;
import ru.java.teamProject.SmartTaskFlow.dto.task.UpdateTaskDTO;
import ru.java.teamProject.SmartTaskFlow.dto.user.UserAssignedToTaskEvent;
import ru.java.teamProject.SmartTaskFlow.dto.user.UserPreviewDTO;
import ru.java.teamProject.SmartTaskFlow.entity.*;
import ru.java.teamProject.SmartTaskFlow.producer.TaskEventProducer;
import ru.java.teamProject.SmartTaskFlow.repository.*;
import ru.java.teamProject.SmartTaskFlow.service.abstr.TaskService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final PanelRepository panelRepository;
    private final UserRepository userRepository;
    private final TaskEventProducer taskEventProducer;


    private TaskDTO buildTaskDto(Task task) {
        return new TaskDTO()
                .setId(task.getId())
                .setName(task.getName())
                .setArchived(task.isArchived())
                .setStartTime(task.getStartDate() != null ? task.getStartDate().toString() : null)
                .setEndTime(task.getEndDate() != null ? task.getEndDate().toString() : null)
                .setDescription(task.getDescription())
                .setPanelId(task.getPanel().getId())
                .setSubTaskIds(task.getSubtasks().stream().map(Subtask::getId).collect(Collectors.toList()));
    }

    private UserPreviewDTO buildPreviewDto(User user){  //нужен, тк по схеме олега должны выводиться только названия задач
        return new UserPreviewDTO()
                .setFirstName(user.getFirstName());
    }


    @Override
    public List<TaskDTO> getTasksInPanel(Long panelId) {
        log.info("Fetching tasks for column ID: {}", panelId);
        return taskRepository.findByPanelId(panelId)
                .stream().map(this::buildTaskDto)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDTO createTask(Long panelId,CreateTaskDTO createTaskDTO) {

        log.info("Creating task in panel ID: {}", panelId);
        Panel panel = panelRepository.findById(panelId)
                .orElseThrow(() -> new IllegalArgumentException("Panel not found"));

        Task task = new Task();
        task.setName(createTaskDTO.getName());
        task.setOrderIndex(createTaskDTO.getOrderIndex());
        task.setDescription(createTaskDTO.getDescription());
        task.setPanel(panel);
        task.setStartDate(createTaskDTO.getStartDate());
        task.setEndDate(createTaskDTO.getEndDate());


        taskRepository.save(task);
        return buildTaskDto(task);
    }
    @Override
    public TaskDTO updateTask(Long taskId, UpdateTaskDTO taskDTO) {
        log.info("Updating task with ID: {}", taskId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        Optional.ofNullable(taskDTO.getName()).ifPresent(task::setName);
        Optional.ofNullable(taskDTO.getOrderIndex()).ifPresent(task::setOrderIndex);
        Optional.ofNullable(taskDTO.getStartDate()).ifPresent(task::setStartDate);
        Optional.ofNullable(taskDTO.getEndDate()).ifPresent(task::setEndDate);
        Optional.ofNullable(taskDTO.getDescription()).ifPresent(task::setDescription); // Исправить все так чтобы можно было стирать все эти значения кроме name

        taskRepository.save(task);
        return buildTaskDto(task);
    }

    @Override
    public void deleteTask(Long taskId) {
        log.info("Deleting task with ID: {}", taskId);
        taskRepository.deleteById(taskId);
    }

    @Override
    public TaskDTO moveTask(Long taskId, Long targetColumnId) {
        log.info("Moving task ID: {} to column ID: {}", taskId, targetColumnId);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        Panel targetColumn = panelRepository.findById(targetColumnId)
                .orElseThrow(() -> new IllegalArgumentException("Target column not found"));

        task.setPanel(targetColumn);
        targetColumn.getTasks().add(task);

        panelRepository.save(targetColumn);
        taskRepository.save(task);

        return buildTaskDto(task);
    }

    @Override
    public TaskDTO assignUserToTask(Long taskId, Long userId) {

        log.info("Assigning user ID: {} to task ID: {}", userId, taskId);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        task.getAssignees().add(user);
        taskRepository.save(task);

        taskEventProducer.sendUserAssignedEvent(new UserAssignedToTaskEvent(
                task.getId(),
                task.getName(),
                user.getEmail()
        ));
        return buildTaskDto(task);
    }

    @Override
    public TaskDTO archiveTask(Long taskId) {

        log.info("Archiving task ID: {}", taskId);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        task.setArchived(true);
        taskRepository.save(task);

        return buildTaskDto(task);
    }

    @Override
    public TaskDTO unArchiveTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        task.setArchived(false);
        taskRepository.save(task);

        return buildTaskDto(task);
    }

    @Override
    public List<Task> getArchivedTasks() {
        return taskRepository.findAllByArchivedTrue();
    }
    @Override
    public List<Task> getNonArchivedTasks() {
        return taskRepository.findAllByArchivedFalse();
    }
    @Override
    public Task getArchivedTaskById(Long id) {
        return taskRepository.findByIdAndArchivedTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Archived task not found"));
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findByIdAndArchivedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
    }

    @Override
    public List<UserPreviewDTO> getUsersInTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        return task.getAssignees()
                .stream()
                .map(user -> new UserPreviewDTO()
                        .setFirstName(user.getFirstName()))
                .collect(Collectors.toList());
    }
}
