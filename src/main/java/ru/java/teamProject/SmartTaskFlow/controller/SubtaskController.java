package ru.java.teamProject.SmartTaskFlow.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.java.teamProject.SmartTaskFlow.dto.subtask.CreateSubTaskDTO;
import ru.java.teamProject.SmartTaskFlow.dto.subtask.UpdateSubTaskDTO;
import ru.java.teamProject.SmartTaskFlow.entity.Subtask;
import ru.java.teamProject.SmartTaskFlow.service.abstr.SubtaskService;

import java.util.List;


@RestController
@RequestMapping("/api/subtasks")
@Tag(name = "Subtask Api", description = "Methods for working with Subtask API")
public class SubtaskController {

    private final SubtaskService subtaskService;

    @Autowired
    public SubtaskController(SubtaskService subtaskService) {
        this.subtaskService = subtaskService;
    }

    @PostMapping("/add")
    public ResponseEntity<Subtask> createSubtask(@RequestBody CreateSubTaskDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(subtaskService.createSubtask(request.getTaskId(), request));
    }

    @PatchMapping("/{subtaskId}")
    public ResponseEntity<Subtask> updateSubtask(@PathVariable Long subtaskId, @RequestBody UpdateSubTaskDTO request) {
        return ResponseEntity
                .ok(subtaskService.updateSubtask(subtaskId, request));
    }

    @DeleteMapping("/{subtaskId}")
    public ResponseEntity<Void> deleteSubtask(@PathVariable Long subtaskId) {
        subtaskService.deleteSubtask(subtaskId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/archive")
    public ResponseEntity<Subtask> archiveSubtask(@PathVariable Long id) {
        Subtask archivedSubtask = subtaskService.archiveSubtask(id);
        return ResponseEntity.ok(archivedSubtask);
    }

    @PatchMapping("/{id}/unarchive")
    public ResponseEntity<Subtask> unArchiveSubtask(@PathVariable Long id) {
        Subtask unArchivedSubtask = subtaskService.unArchiveSubtask(id);
        return ResponseEntity.ok(unArchivedSubtask);
    }
    @GetMapping("/archived")
    public ResponseEntity<List<Subtask>> getArchivedSubtasks() {
        return ResponseEntity.ok(subtaskService.getArchivedSubtasks());
    }

    @GetMapping("/non-archived")
    public ResponseEntity<List<Subtask>> getNonArchivedSubtasks() {
        return ResponseEntity.ok(subtaskService.getNonArchivedSubtasks());
    }


    //
    @GetMapping("/getSubtask/byTask/{taskId}")
    public ResponseEntity<?> getSubtaskByTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(subtaskService.getSubtaskByTask(taskId));
    }
}