package ru.java.teamProject.SmartTaskFlow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.java.teamProject.SmartTaskFlow.entity.Task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByPanelId(Long panelId);
    List<Task> findAllByArchivedTrue();
    List<Task> findAllByArchivedFalse();
    Optional<Task> findByIdAndArchivedTrue(Long id);
    Optional<Task> findByIdAndArchivedFalse(Long id);

    List<Task> findByEndDateBetween(LocalDateTime from, LocalDateTime to);

}