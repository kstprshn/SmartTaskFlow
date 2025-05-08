package ru.java.teamProject.SmartTaskFlow.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.java.teamProject.SmartTaskFlow.dto.subtask.CreateSubTaskDTO;
import ru.java.teamProject.SmartTaskFlow.dto.task.CreateTaskDTO;
import ru.java.teamProject.SmartTaskFlow.dto.task.TaskDTO;
import ru.java.teamProject.SmartTaskFlow.dto.task.UpdateTaskDTO;
import ru.java.teamProject.SmartTaskFlow.dto.user.UserPreviewDTO;
import ru.java.teamProject.SmartTaskFlow.entity.Task;
import ru.java.teamProject.SmartTaskFlow.service.abstr.SubtaskService;
import ru.java.teamProject.SmartTaskFlow.service.abstr.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Task Api", description = "Methods for working with Task API")
public class TaskController {

    private final TaskService taskService;
    private final SubtaskService subtaskService;

    @Autowired
    public TaskController(TaskService taskService, SubtaskService subtaskService) {
        this.taskService = taskService;
        this.subtaskService = subtaskService;
    }

    @PatchMapping("/tasks/{taskId}/edit")
    public ResponseEntity<TaskDTO> updateTask(
            @PathVariable Long taskId,
            @Valid @RequestBody UpdateTaskDTO updateTaskDTO
    ) {
        TaskDTO updatedTask = taskService.updateTask(taskId, updateTaskDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedTask);
    }

    @DeleteMapping("/tasks/tasks/{taskId}")
    public ResponseEntity<HttpStatus> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/tasks/tasks/{taskId}/move")
    public ResponseEntity<?> moveTask(@PathVariable Long taskId, @RequestParam Long targetColumnId) {
        return ResponseEntity.ok(taskService.moveTask(taskId, targetColumnId));
    }

    @PatchMapping("/tasks/{taskId}/assign/{userId}")
    public ResponseEntity<?> assignUser(@PathVariable Long taskId, @PathVariable Long userId) {
        return ResponseEntity.ok(taskService.assignUserToTask(taskId, userId));
    }

    // checklist
    @PostMapping("/tasks/tasks/{taskId}/subtasks")
    public ResponseEntity<?> addSubTask(@PathVariable Long taskId, @Valid @RequestBody CreateSubTaskDTO subTaskDTO) {
        return ResponseEntity.ok(subtaskService.createSubtask(taskId, subTaskDTO));
    }


    @PatchMapping("/tasks/{taskId}/archive")
    public ResponseEntity<?> archiveTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.archiveTask(taskId));
    }

    @PatchMapping ("/tasks/{taskId}/unarchive")
    public ResponseEntity<?> unArchiveTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.unArchiveTask(taskId));
    }
    @GetMapping("/tasks/archived")
    public ResponseEntity<List<Task>> getArchivedTasks() {
        return ResponseEntity.ok(taskService.getArchivedTasks());
    }

    @GetMapping("/tasks/non-archived")
    public ResponseEntity<List<Task>> getNonArchivedTasks() {
        return ResponseEntity.ok(taskService.getNonArchivedTasks());
    }

    @GetMapping("/tasks/archived/{id}")
    public ResponseEntity<Task> getArchivedTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getArchivedTaskById(id));
    }

    @GetMapping("/tasks/getTask/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @GetMapping("/tasks/{taskId}/getMembers")
    public ResponseEntity<List<UserPreviewDTO>> getUsersInTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.getUsersInTask(taskId));
    }

    /// ---------------------------------------------------------------------------

    @GetMapping("/panels/{panelId}/task")
    public ResponseEntity<?> getTaskFromPanel(@PathVariable Long panelId) {
        return ResponseEntity.ok(taskService.getTasksInPanel(panelId));
    }

    @PostMapping("/panels/{panelId}/task")
    public ResponseEntity<TaskDTO> createTask(
            @PathVariable Long panelId,
            @Valid @RequestBody CreateTaskDTO createTaskDTO
    ) {
        System.out.println("I'm here");
        TaskDTO createdTask = taskService.createTask(panelId, createTaskDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }
}


