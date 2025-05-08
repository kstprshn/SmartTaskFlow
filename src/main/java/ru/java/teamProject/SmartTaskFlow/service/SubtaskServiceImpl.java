package ru.java.teamProject.SmartTaskFlow.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.java.teamProject.SmartTaskFlow.dto.subtask.CreateSubTaskDTO;
import ru.java.teamProject.SmartTaskFlow.dto.subtask.UpdateSubTaskDTO;
import ru.java.teamProject.SmartTaskFlow.entity.Subtask;
import ru.java.teamProject.SmartTaskFlow.entity.Task;
import ru.java.teamProject.SmartTaskFlow.repository.SubtaskRepository;
import ru.java.teamProject.SmartTaskFlow.repository.TaskRepository;
import ru.java.teamProject.SmartTaskFlow.service.abstr.SubtaskService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class SubtaskServiceImpl implements SubtaskService {

    private final SubtaskRepository subtaskRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public SubtaskServiceImpl(SubtaskRepository subtaskRepository, TaskRepository taskRepository) {
        this.subtaskRepository = subtaskRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public Subtask createSubtask(Long taskId, CreateSubTaskDTO request) {
        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new NoSuchElementException("Task not found"));
        Subtask subtask = new Subtask();
        subtask.setName(request.getName());
        subtask.setTask(task);
        return subtaskRepository.save(subtask);
    }

    @Override
    public Subtask updateSubtask(Long subtaskId, UpdateSubTaskDTO request) {
        Subtask subtask = subtaskRepository.findById(subtaskId).orElseThrow(
                () -> new NoSuchElementException("Subtask not found"));
        Optional.ofNullable(request.getName()).ifPresent(subtask::setName);
        subtask.setCompleted(request.isCompleted());
        return subtaskRepository.save(subtask);
    }

    public void deleteSubtask(Long subtaskId) {
        subtaskRepository.deleteById(subtaskId);
    }
    @Override
    public Subtask archiveSubtask(Long id) {
        Subtask subtask = subtaskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Subtask not found"));

        subtask.setArchived(true);
        return subtaskRepository.save(subtask);
    }

    @Override
    public Subtask unArchiveSubtask(Long id) {
        Subtask subtask = subtaskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Subtask not found"));

        subtask.setArchived(false);
        return subtaskRepository.save(subtask);
    }

    @Override
    public List<Subtask> getArchivedSubtasks() {
        return subtaskRepository.findAllByArchivedTrue();
    }

    @Override
    public List<Subtask> getNonArchivedSubtasks() {
        return subtaskRepository.findAllByArchivedFalse();
    }

    @Override
    public List<Subtask> getSubtaskByTask(Long taskId) {
        Task task = taskRepository.getReferenceById(taskId);

        return subtaskRepository.findAllByTask(task);
    }

    @Override
    public List<Subtask> getArchivedSubtaskByTaskId(Long taskId) {
        Task task = taskRepository.getReferenceById(taskId);

        return subtaskRepository.findAllByTaskAndArchivedTrue(task);
    }

    @Override
    public List<Subtask> getNonArchivedSubtaskByTaskId(Long taskId) {
        Task task = taskRepository.getReferenceById(taskId);

        return subtaskRepository.findAllByTaskAndArchivedFalse(task);
    }
}
