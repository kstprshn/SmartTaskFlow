package ru.java.teamProject.SmartTaskFlow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.java.teamProject.SmartTaskFlow.dto.panel.CreatePanelDTO;
import ru.java.teamProject.SmartTaskFlow.dto.panel.PanelUpdateDTO;
import ru.java.teamProject.SmartTaskFlow.entity.Panel;
import ru.java.teamProject.SmartTaskFlow.service.abstr.PanelService;

import java.util.List;


@RestController
@RequestMapping("/api")
public class PanelController {

    private final PanelService panelService;

    @Autowired
    public PanelController(PanelService panelService) {
        this.panelService = panelService;
    }


    @GetMapping("/panels/all")
    public ResponseEntity<List<Panel>> getAllPanels() {
        List<Panel> panels = panelService.getNonArchivedPanels();
        return ResponseEntity.status(HttpStatus.OK).body(panels);
    }

    @GetMapping("/panels/getPanel/{panelId}")
    public ResponseEntity<Panel> getPanel(@PathVariable Long panelId){
        Panel panelById = panelService.getPanelById(panelId);
        return new ResponseEntity<>(panelById, HttpStatus.OK);
    }


    @PutMapping("/panels/{panelId}")
    public ResponseEntity<Panel> updatePanel(@PathVariable Long panelId, @RequestBody PanelUpdateDTO request) {
        return ResponseEntity
                .ok(panelService.updatePanel(panelId, request.getName()));
    }

    @PatchMapping("/panels/{id}/archive")
    public ResponseEntity<Panel> archivePanel(@PathVariable Long id) {
        Panel archivedPanel = panelService.archivePanel(id);
        return ResponseEntity.ok(archivedPanel);
    }

    @PatchMapping("/panels/{id}/unarchive")
    public ResponseEntity<Panel> unArchivePanel(@PathVariable Long id) {
        Panel unArchivedPanel = panelService.unArchivePanel(id);
        return ResponseEntity.ok(unArchivedPanel);
    }

    @DeleteMapping("/panels/{panelId}")
    public ResponseEntity<Void> deletePanel(@PathVariable Long panelId) {
        panelService.deletePanel(panelId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/panels/archived")
    public ResponseEntity<List<Panel>> getArchivedPanels() {
        return ResponseEntity.ok(panelService.getArchivedPanels());
    }

    @GetMapping("/panels/archived/{id}")
    public ResponseEntity<Panel> getArchivedPanelById(@PathVariable Long id) {
        return ResponseEntity.ok(panelService.getArchivedPanelById(id));
    }

    // Get /api/board/{boardId}/panels -> Список <дто панелей> на доске

    @PostMapping("/boards/{boardId}/panels")
    public ResponseEntity<Panel> createPanel(@RequestBody CreatePanelDTO request, @PathVariable Long boardId) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(panelService.createPanel(boardId, request));
    }

    @GetMapping("/boards/{boardId}/panels")
    public ResponseEntity<List<Panel>> getAllPanelByBoardId(@PathVariable Long boardId) {
        List<Panel> panels = panelService.getPanels(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(panels);
    }

    @GetMapping("/boards/{boardId}/panels/archived")
    public ResponseEntity<List<Panel>> getArchivedPanelByBoardId(@PathVariable Long boardId) {
        List<Panel> panels = panelService.getArchivedPanels(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(panels);
    }

    @GetMapping("/boards/{boardId}/panels/non-archived")
    public ResponseEntity<List<Panel>> getNonArchivedPanelByBoardId(@PathVariable Long boardId) {
        List<Panel> panels = panelService.getNonArchivedPanels(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(panels);
    }

}
