package ru.java.teamProject.SmartTaskFlow.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.java.teamProject.SmartTaskFlow.dto.comment.CommentResponse;
import ru.java.teamProject.SmartTaskFlow.dto.comment.CommentUpdateDto;
import ru.java.teamProject.SmartTaskFlow.dto.comment.CreateCommentDTO;
import ru.java.teamProject.SmartTaskFlow.entity.Comment;
import ru.java.teamProject.SmartTaskFlow.service.abstr.CommentService;

import java.util.List;

@RestController
@Tag(name = "Comment Api", description = "Methods for working with Comment API")
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{task_id}/getAll")
    @Operation(summary = "Receiving all comments of the task")
    public ResponseEntity<List<CommentResponse>> getAllComments(@PathVariable("task_id") Long task_id) {
        List<CommentResponse> comments = commentService.getAllComments(task_id);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{comment_id}/get")
    @Operation(summary = "Receiving the comment")
    public ResponseEntity<CommentResponse> getCommentById(@PathVariable Long comment_id) {
        CommentResponse comment = commentService.getCommentById(comment_id);
        return ResponseEntity.ok(comment);
    }

    @PatchMapping("/{id}/edit")
    @Operation(summary = "Editing the comment")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long id,
            @RequestBody @Valid CommentUpdateDto request
            ) {
        CommentResponse updatedComment = commentService.updateComment(id, request.getNewContent());
        return ResponseEntity.status(HttpStatus.OK).body(updatedComment);
    }

    @PostMapping("/api/tasks/{taskId}/comments/add")
    @Operation(summary = "Creating the comment")
    public ResponseEntity<Comment> addComment(@PathVariable Long taskId, @RequestBody @Valid CreateCommentDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentService.createComment(taskId, request.getAuthorId(), request.getContent()));
    }

    @DeleteMapping("/{commentId}/delete")
    @Operation(summary = "Deleting the comment")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}