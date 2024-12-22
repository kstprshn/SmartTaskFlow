package ru.java.teamProject.SmartTaskFlow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.java.teamProject.SmartTaskFlow.entity.Comment;
import ru.java.teamProject.SmartTaskFlow.entity.Task;
import ru.java.teamProject.SmartTaskFlow.entity.User;
import ru.java.teamProject.SmartTaskFlow.repository.CommentRepository;
import ru.java.teamProject.SmartTaskFlow.repository.TaskRepository;
import ru.java.teamProject.SmartTaskFlow.repository.UserRepository;
import ru.java.teamProject.SmartTaskFlow.service.abstr.CommentService;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;


@Service
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

    public Comment addComment(Long taskId, Long authorId, String content) {
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

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
