package ru.java.teamProject.SmartTaskFlow.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.java.teamProject.SmartTaskFlow.dto.comment.CreateCommentDTO;
import ru.java.teamProject.SmartTaskFlow.dto.subtask.CreateSubTaskDTO;
import ru.java.teamProject.SmartTaskFlow.dto.subtask.SubTaskDTO;
import ru.java.teamProject.SmartTaskFlow.dto.subtask.UpdateSubTaskDTO;
import ru.java.teamProject.SmartTaskFlow.dto.task.CreateTaskDTO;
import ru.java.teamProject.SmartTaskFlow.dto.task.TaskDTO;
import ru.java.teamProject.SmartTaskFlow.dto.task.UpdateTaskDTO;
import ru.java.teamProject.SmartTaskFlow.entity.*;
import ru.java.teamProject.SmartTaskFlow.repository.*;
import ru.java.teamProject.SmartTaskFlow.service.abstr.TaskService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@Transactional
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final PanelRepository panelRepository;
    private final UserRepository userRepository;


    private TaskDTO buildTaskDto(Task task) {
        return new TaskDTO()
                .setId(task.getId())
                .setName(task.getName())
                .setPriority(task.getPriority())
                .setArchived(task.isArchived())
                .setStartTime(task.getStartDate().toString())
                .setEndTime(task.getEndDate().toString())
                .setPanelId(task.getPanel().getId());
    }

    public TaskServiceImpl(TaskRepository taskRepository, PanelRepository panelRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.panelRepository = panelRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<TaskDTO> getTasksInColumn(Long columnId) {
        log.info("Fetching tasks for column ID: {}", columnId);
        return taskRepository.findByPanelId(columnId)
                .stream().map(this::buildTaskDto)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDTO addTaskToColumn(Long panelId, CreateTaskDTO taskDTO) {
        log.info("Adding task to column ID: {}", panelId);
        Panel panel = panelRepository.findById(panelId)
                .orElseThrow(() -> new IllegalArgumentException("Column not found"));
        Task task = new Task();
        task.setName(taskDTO.getName());
        task.setPriority(taskDTO.getPriority());
        task.setOrderIndex(taskDTO.getOrderIndex());
        task.setPanel(panel);
        taskRepository.save(task);
        return null;
    }

    @Override
    public TaskDTO updateTask(Long taskId, UpdateTaskDTO taskDTO) {
        log.info("Updating task with ID: {}", taskId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        if (taskDTO.getName() != null) {
            task.setName(taskDTO.getName());
        }
        if (taskDTO.getPriority() != null) {
            task.setPriority(taskDTO.getPriority());
        }
        if (taskDTO.getOrderIndex() != null) {
            task.setOrderIndex(taskDTO.getOrderIndex());
        }
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
    public TaskDTO addCommentToTask(Long taskId, CreateCommentDTO commentDTO) {
        log.info("Adding comment to task ID: {}", taskId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        comment.setTask(task);
        task.getComments().add(comment);
        taskRepository.save(task);

        return buildTaskDto(task);
    }

    @Override
    public List<TaskDTO> getArchivedTasks(String email) {
        log.info("Fetching archived tasks for user: {}", email);

        return new ArrayList<>();
//        return taskRepository.findByCreatorEmailAndArchived(email, true)
//                .stream()
//                .map(task -> {
//                    TaskDTO taskDTO = new TaskDTO();
//                    taskDTO.setId(task.getId());
//                    taskDTO.setName(task.getName());
//                    taskDTO.setArchived(true);
//                    return taskDTO;
//                })
//                .toList();
    }

    @Override
    public TaskDTO createTask(Long panelId, String name, String priority, Integer orderIndex) {
        log.info("Creating task in panel ID: {}", panelId);

        // Найти панель по ID
        Panel panel = panelRepository.findById(panelId)
                .orElseThrow(() -> new IllegalArgumentException("Panel not found"));

        // Создать задачу и заполнить поля
        Task task = new Task();
        task.setName(name);
        task.setPriority(priority);
        task.setOrderIndex(orderIndex);
        task.setPanel(panel);

        taskRepository.save(task);
        return buildTaskDto(task);
    }

    @Override
    public TaskDTO assignUser(Long taskId, Long userId) {
        log.info("Assigning user ID: {} to task ID: {}", userId, taskId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        task.getAssignees().add(user);
        taskRepository.save(task);

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
}
