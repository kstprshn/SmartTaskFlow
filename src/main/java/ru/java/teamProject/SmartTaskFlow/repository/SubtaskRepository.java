package ru.java.teamProject.SmartTaskFlow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.java.teamProject.SmartTaskFlow.entity.Subtask;
import ru.java.teamProject.SmartTaskFlow.entity.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubtaskRepository extends JpaRepository<Subtask, Long> {
    List<Subtask> findAllByArchivedTrue();

    List<Subtask> findAllByArchivedFalse();

    List<Subtask> findAllByTask(Task task);
    List<Subtask> findAllByTaskAndArchivedTrue(Task task);
    List<Subtask> findAllByTaskAndArchivedFalse(Task task);

}