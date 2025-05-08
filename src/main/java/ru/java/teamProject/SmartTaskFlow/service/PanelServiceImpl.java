package ru.java.teamProject.SmartTaskFlow.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.java.teamProject.SmartTaskFlow.dto.panel.CreatePanelDTO;
import ru.java.teamProject.SmartTaskFlow.entity.Board;
import ru.java.teamProject.SmartTaskFlow.entity.Panel;
import ru.java.teamProject.SmartTaskFlow.repository.BoardRepository;
import ru.java.teamProject.SmartTaskFlow.repository.PanelRepository;
import ru.java.teamProject.SmartTaskFlow.service.abstr.PanelService;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@Slf4j
public class PanelServiceImpl implements PanelService {

    private final PanelRepository panelRepository;
    private final BoardRepository boardRepository;

    public PanelServiceImpl(PanelRepository panelRepository, BoardRepository boardRepository) {
        this.panelRepository = panelRepository;
        this.boardRepository = boardRepository;
    }

    @Override
    public Panel createPanel(Long boardId, CreatePanelDTO request) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("Board not found"));
        Panel panel = new Panel();
        panel.setName(request.getName());
        panel.setOrderIndex(request.getOrderIndex());
        panel.setBoard(board);
        return panelRepository.save(panel);
    }

    @Override
    public Panel updatePanel(Long panelId, String newName) {
        Panel panel = panelRepository.findById(panelId).orElseThrow(
                () -> new NoSuchElementException("Panel not found"));
        panel.setName(newName);
        return panelRepository.save(panel);
    }

    @Override
    public void deletePanel(Long panelId) {
        panelRepository.deleteById(panelId);
    }

    @Override
    public Panel archivePanel(Long id) {
        Panel panel = panelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Panel not found"));

        panel.setArchived(true);
        return panelRepository.save(panel);
    }

    @Override
    public Panel unArchivePanel(Long id) {
        Panel panel = panelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Panel not found"));

        panel.setArchived(false);
        return panelRepository.save(panel);
    }

    @Override
    public List<Panel> getArchivedPanels(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow();
        return panelRepository.findAllByBoardAndArchivedTrue(board);
    }

    @Override
    public List<Panel> getNonArchivedPanels(Long boardId) {
        return panelRepository.findAllByBoardIdAndArchivedFalse(boardId);
    }


    @Override
    public List<Panel> getPanelsByBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow();

        return board.getPanels();
    }

    @Override
    public Panel getArchivedPanelById(Long id) {
        return panelRepository.findByIdAndArchivedTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Archived panel not found"));
    }

    @Override
    public Panel getPanelById(Long id) {
        return panelRepository.findByIdAndArchivedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Panel not found"));
    }
}
