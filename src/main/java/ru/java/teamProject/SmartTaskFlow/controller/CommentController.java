package ru.java.teamProject.SmartTaskFlow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.java.teamProject.SmartTaskFlow.dto.comment.AddCommentDTO;
import ru.java.teamProject.SmartTaskFlow.entity.Comment;
import ru.java.teamProject.SmartTaskFlow.service.abstr.CommentService;

@RestController
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/api/tasks/{taskId}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable Long taskId, @RequestBody AddCommentDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentService.addComment(taskId, request.getAuthorId(), request.getContent()));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}