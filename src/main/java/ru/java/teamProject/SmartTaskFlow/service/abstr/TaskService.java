package ru.java.teamProject.SmartTaskFlow.service.abstr;

import ru.java.teamProject.SmartTaskFlow.dto.task.CreateTaskDTO;
import ru.java.teamProject.SmartTaskFlow.dto.task.TaskDTO;
import ru.java.teamProject.SmartTaskFlow.dto.task.UpdateTaskDTO;
import ru.java.teamProject.SmartTaskFlow.dto.user.UserPreviewDTO;
import ru.java.teamProject.SmartTaskFlow.entity.Task;

import java.util.List;


public interface TaskService {
    List<TaskDTO> getTasksInPanel(Long columnId);
    TaskDTO updateTask(Long taskId, UpdateTaskDTO taskDTO);
    void deleteTask(Long taskId);
    TaskDTO moveTask(Long taskId, Long targetColumnId);
    TaskDTO createTask(Long panelId,CreateTaskDTO createTaskDTO);
    TaskDTO assignUserToTask(Long taskId, Long userId);
    TaskDTO archiveTask(Long taskId);
    TaskDTO unArchiveTask(Long taskId);
    List<Task> getArchivedTasks();
    List<Task> getNonArchivedTasks();
    Task getArchivedTaskById(Long id);
    Task getTaskById(Long id);
    List<UserPreviewDTO> getUsersInTask(Long taskId);
}
