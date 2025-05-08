package ru.java.teamProject.SmartTaskFlow.service.abstr;

import ru.java.teamProject.SmartTaskFlow.dto.panel.CreatePanelDTO;
import ru.java.teamProject.SmartTaskFlow.entity.Panel;

import java.util.List;

public interface PanelService { // RETURN PANEL DTO NOT A PANEL
    Panel createPanel(Long boardId, CreatePanelDTO request);
    Panel updatePanel(Long panelId, String newName);
    void deletePanel(Long panelId);
    Panel archivePanel(Long id);
    Panel unArchivePanel(Long id);

    List<Panel> getPanelsByBoard(Long boardId);
    List<Panel> getArchivedPanels(Long boardId);
    List<Panel> getNonArchivedPanels(Long boardId);


    Panel getArchivedPanelById(Long id);
    Panel getPanelById(Long id);
}
