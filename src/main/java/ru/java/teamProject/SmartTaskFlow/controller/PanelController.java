package ru.java.teamProject.SmartTaskFlow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.java.teamProject.SmartTaskFlow.dto.panel.CreatePanelDTO;
import ru.java.teamProject.SmartTaskFlow.dto.panel.PanelUpdateDTO;
import ru.java.teamProject.SmartTaskFlow.entity.Panel;
import ru.java.teamProject.SmartTaskFlow.service.abstr.PanelService;


@RestController
@RequestMapping("/api/panels")
public class PanelController {

    private final PanelService panelService;

    @Autowired
    public PanelController(PanelService panelService) {
        this.panelService = panelService;
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

    @DeleteMapping("/{panelId}")
    public ResponseEntity<Void> deletePanel(@PathVariable Long panelId) {
        panelService.deletePanel(panelId);
        return ResponseEntity.noContent().build();
    }
}
