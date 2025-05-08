package ru.java.teamProject.SmartTaskFlow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.java.teamProject.SmartTaskFlow.entity.Board;
import ru.java.teamProject.SmartTaskFlow.entity.Panel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PanelRepository extends JpaRepository<Panel, Long> {
    List<Panel> findAllByArchivedTrue();

    List<Panel> findAllByArchivedFalse();

    List<Panel> findAllByBoardIdAndArchivedFalse(Long boardId);

    List<Panel> findAllByBoardAndArchivedTrue(Board board);

    Optional<Panel> findByIdAndArchivedTrue(Long id);
    Optional<Panel> findByIdAndArchivedFalse(Long id);
}