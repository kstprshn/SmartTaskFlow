package ru.java.teamProject.SmartTaskFlow.service.abstr;

import ru.java.teamProject.SmartTaskFlow.dto.subtask.CreateSubTaskDTO;
import ru.java.teamProject.SmartTaskFlow.dto.task.TaskDTO;
import ru.java.teamProject.SmartTaskFlow.entity.Subtask;

import java.util.List;


public interface SubtaskService {
    Subtask createSubtask(Long taskId, CreateSubTaskDTO request);
    Subtask updateSubtask(Long subtaskId, CreateSubTaskDTO request);
    void deleteSubtask(Long subtaskId);
    Subtask archiveSubtask(Long id);
    Subtask unArchiveSubtask(Long id);
    List<Subtask> getArchivedSubtasks();
    List<Subtask> getNonArchivedSubtasks();
    Subtask getArchivedSubtaskById(Long id);
    Subtask getNonArchivedSubtaskById(Long id);

}

