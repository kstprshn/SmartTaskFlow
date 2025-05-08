package ru.java.teamProject.SmartTaskFlow.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.java.teamProject.SmartTaskFlow.dto.comment.CommentResponse;
import ru.java.teamProject.SmartTaskFlow.entity.Comment;
import ru.java.teamProject.SmartTaskFlow.entity.Task;
import ru.java.teamProject.SmartTaskFlow.entity.User;
import ru.java.teamProject.SmartTaskFlow.repository.CommentRepository;
import ru.java.teamProject.SmartTaskFlow.repository.TaskRepository;
import ru.java.teamProject.SmartTaskFlow.repository.UserRepository;
import ru.java.teamProject.SmartTaskFlow.service.abstr.CommentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;


@Service
@Slf4j
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, TaskRepository taskRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    private CommentResponse buildDTO(Comment comment) {
        return new CommentResponse()
                .setId(comment.getId())
                .setContent(comment.getContent())
                .setAuthorUsername(comment.getAuthor().getUsername())
                .setTaskId(comment.getTask().getId())
                .setCreatedAt(comment.getCreatedDate());
    }

    @Override
    public List<CommentResponse> getAllComments(Long taskId) {
        log.info("Fetching all comments");
        return commentRepository.findAllByTaskId(taskId).stream()
                .map(this::buildDTO)
                .toList();
    }

    @Override
    public CommentResponse getCommentById(Long commentId) {
        log.info("Fetching comment with ID: {}", commentId);
        return commentRepository.findById(commentId)
                .map(this::buildDTO)
                .orElseThrow(() -> new NoSuchElementException("Comment not found"));
    }

    @Override
    public CommentResponse updateComment(Long commentId, String newContent) {
        log.info("Updating comment ID: {}", commentId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("Comment not found"));

        comment.setContent(newContent);
        commentRepository.save(comment);
        return buildDTO(comment);
    }

    @Override
    public Comment createComment(Long taskId, Long authorId, String content) {
        Task task = taskRepository.findById(taskId).
                orElseThrow(() -> new NoSuchElementException("Task not found"));
        User author = userRepository.findById(authorId).
                orElseThrow(() -> new NoSuchElementException("User not found"));
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setAuthor(author);
        comment.setTask(task);
        comment.setCreatedDate(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
