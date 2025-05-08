package ru.java.teamProject.SmartTaskFlow.service.abstr;

import ru.java.teamProject.SmartTaskFlow.dto.comment.CommentResponse;
import ru.java.teamProject.SmartTaskFlow.entity.Comment;

import java.util.List;


public interface CommentService {
    List<CommentResponse> getAllComments(Long task_id);
    CommentResponse getCommentById(Long commentId);
    CommentResponse updateComment(Long commentId, String newContent);
    Comment createComment(Long taskId, Long authorId, String content);
    void deleteComment(Long commentId);
}
