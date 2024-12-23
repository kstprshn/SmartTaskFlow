package ru.java.teamProject.SmartTaskFlow.service.abstr;

import ru.java.teamProject.SmartTaskFlow.dto.task.TaskDTO;
import ru.java.teamProject.SmartTaskFlow.entity.Panel;

import java.util.List;

public interface PanelService {
    Panel createPanel(Long boardId, String name, Integer orderIndex);

    Panel updatePanel(Long panelId, String newName);

    void deletePanel(Long panelId);

    List<Panel> getAll();

    Panel getOnePanel(Long id);

    Panel archivePanel(Long id);

    Panel unArchivePanel(Long id);

    List<Panel> getArchivedPanels();

    List<Panel> getNonArchivedPanels();
    Panel getArchivedPanelById(Long id);
}
