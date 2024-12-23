package ru.java.teamProject.SmartTaskFlow.service.abstr;

import ru.java.teamProject.SmartTaskFlow.entity.Panel;

import java.util.List;

public interface PanelService {
    Panel createPanel(Long boardId, String name, Integer orderIndex);
    Panel updatePanel(Long panelId, String newName);
    void deletePanel(Long panelId);
    Panel archivePanel(Long id);
    Panel unArchivePanel(Long id);
    List<Panel> getArchivedPanels();

    List<Panel> getNonArchivedPanels();
    Panel getArchivedPanelById(Long id);
    Panel getPanelById(Long id);
}
