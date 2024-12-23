package ru.java.teamProject.SmartTaskFlow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import ru.java.teamProject.SmartTaskFlow.dto.board.BoardDTO;
import ru.java.teamProject.SmartTaskFlow.dto.panel.CreatePanelDTO;
import ru.java.teamProject.SmartTaskFlow.dto.panel.PanelUpdateDTO;
import ru.java.teamProject.SmartTaskFlow.entity.Panel;
import ru.java.teamProject.SmartTaskFlow.service.abstr.PanelService;

import java.util.List;


@RestController
@RequestMapping("/api/panels")
public class PanelController {

    private final PanelService panelService;

    @Autowired
    public PanelController(PanelService panelService) {
        this.panelService = panelService;
    }


    @GetMapping("/all")
    public ResponseEntity<List<Panel>> getAllPanels() {
        List<Panel> panels = panelService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(panels);
    }

    @GetMapping("/getPanel/{panelId}")
    public ResponseEntity<Panel> getPanel(@PathVariable Long panelId){
        Panel panelById = panelService.getOnePanel(panelId);
        return new ResponseEntity<>(panelById, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Panel> createPanel(@RequestBody CreatePanelDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(panelService.createPanel(request.getBoardId(), request.getName(), request.getOrderIndex()));
    }

    @PutMapping("/{panelId}")
    public ResponseEntity<Panel> updatePanel(@PathVariable Long panelId, @RequestBody PanelUpdateDTO request) {
        return ResponseEntity
                .ok(panelService.updatePanel(panelId, request.getName()));
    }

    @PatchMapping("/{id}/archive")
    public ResponseEntity<Panel> archivePanel(@PathVariable Long id) {
        Panel archivedPanel = panelService.archivePanel(id);
        return ResponseEntity.ok(archivedPanel);
    }

    @PatchMapping("/{id}/unarchive")
    public ResponseEntity<Panel> unArchivePanel(@PathVariable Long id) {
        Panel unArchivedPanel = panelService.unArchivePanel(id);
        return ResponseEntity.ok(unArchivedPanel);
    }

    @DeleteMapping("/{panelId}")
    public ResponseEntity<Void> deletePanel(@PathVariable Long panelId) {
        panelService.deletePanel(panelId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/archived")
    public ResponseEntity<List<Panel>> getArchivedPanels() {
        return ResponseEntity.ok(panelService.getArchivedPanels());
    }

    @GetMapping("/non-archived")
    public ResponseEntity<List<Panel>> getNonArchivedPanels() {
        return ResponseEntity.ok(panelService.getNonArchivedPanels());
    }

    @GetMapping("/archived/{id}")
    public ResponseEntity<Panel> getArchivedPanelById(@PathVariable Long id) {
        return ResponseEntity.ok(panelService.getArchivedPanelById(id));
    }
}
