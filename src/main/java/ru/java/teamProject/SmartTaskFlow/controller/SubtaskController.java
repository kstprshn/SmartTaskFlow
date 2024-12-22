package ru.java.teamProject.SmartTaskFlow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.java.teamProject.SmartTaskFlow.dto.subtask.CreateSubTaskDTO;
import ru.java.teamProject.SmartTaskFlow.entity.Subtask;
import ru.java.teamProject.SmartTaskFlow.service.abstr.SubtaskService;


@RestController
@RequestMapping("/api/subtasks")
public class SubtaskController {

    private final SubtaskService subtaskService;

    @Autowired
    public SubtaskController(SubtaskService subtaskService) {
        this.subtaskService = subtaskService;
    }

    @PostMapping
    public ResponseEntity<Subtask> createSubtask(@RequestBody CreateSubTaskDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(subtaskService.createSubtask(request.getTaskId(), request));
    }

    @PutMapping("/{subtaskId}")
    public ResponseEntity<Subtask> updateSubtask(@PathVariable Long subtaskId, @RequestBody CreateSubTaskDTO request) {
        return ResponseEntity
                .ok(subtaskService.updateSubtask(subtaskId, request));
    }

    @DeleteMapping("/{subtaskId}")
    public ResponseEntity<Void> deleteSubtask(@PathVariable Long subtaskId) {
        subtaskService.deleteSubtask(subtaskId);
        return ResponseEntity.noContent().build();
    }
}