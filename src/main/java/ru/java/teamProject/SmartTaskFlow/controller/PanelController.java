package ru.java.teamProject.SmartTaskFlow.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Tasks panel Api", description = "Methods for working with panel API")
public class PanelController {

    private final PanelService panelService;

    @Autowired
    public PanelController(PanelService panelService) {
        this.panelService = panelService;
    }


//    @GetMapping("/panels/all")
//    public ResponseEntity<List<Panel>> getAllPanels() {
//        List<Panel> panels = panelService.getNonArchivedPanels();
//        return ResponseEntity.status(HttpStatus.OK).body(panels);
//    }

    @GetMapping("/panels/{panelId}/getPanel")
    @Operation(summary = "Getting the board panel")
    public ResponseEntity<Panel> getNonArchivedPanelById(@PathVariable Long panelId){
        Panel panelById = panelService.getPanelById(panelId);
        return new ResponseEntity<>(panelById, HttpStatus.OK);
    }


    @PutMapping("/panels/{panelId}")
    @Operation(summary = "Updating the board panel")
    public ResponseEntity<Panel> updatePanel(@PathVariable Long panelId, @RequestBody PanelUpdateDTO request) {
        return ResponseEntity
                .ok(panelService.updatePanel(panelId, request.getName()));
    }

    @PatchMapping("/panels/{id}/archive")
    @Operation(summary = "Archiving the panel")
    public ResponseEntity<Panel> archivePanel(@PathVariable Long id) {
        Panel archivedPanel = panelService.archivePanel(id);
        return ResponseEntity.ok(archivedPanel);
    }

    @PatchMapping("/panels/{id}/unarchive")
    @Operation(summary = "Unarchiving the panel")
    public ResponseEntity<Panel> unArchivePanel(@PathVariable Long id) {
        Panel unArchivedPanel = panelService.unArchivePanel(id);
        return ResponseEntity.ok(unArchivedPanel);
    }

    @DeleteMapping("/panels/{panelId}")
    @Operation(summary = "Deleting the panel")
    public ResponseEntity<Void> deletePanel(@PathVariable Long panelId) {
        panelService.deletePanel(panelId);
        return ResponseEntity.noContent().build();
    }

    //ненужный метод, тк внизу есть правильная версия метода
//    @GetMapping("/panels/archived")
//    public ResponseEntity<List<Panel>> getArchivedPanels() {
//        return ResponseEntity.ok(panelService.getArchivedPanels());
//    }

    @GetMapping("/panels/archived/{id}")
    @Operation(summary = "Getting an archived panel")
    public ResponseEntity<Panel> getArchivedPanelById(@PathVariable Long id) {
        return ResponseEntity.ok(panelService.getArchivedPanelById(id));
    }

    // Get /api/board/{boardId}/panels -> Список <дто панелей> на доске

    @PostMapping("/boards/{boardId}/panels/add")
    @Operation(summary = "Creating a new panel")
    public ResponseEntity<Panel> createPanel(@RequestBody CreatePanelDTO request, @PathVariable Long boardId) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(panelService.createPanel(boardId, request));
    }

//    @GetMapping("/boards/{boardId}/panels")
//    public ResponseEntity<List<Panel>> getAllPanelByBoardId(@PathVariable Long boardId) {
//        List<Panel> panels = panelService.getPanelsByBoard(boardId);
//        return ResponseEntity.status(HttpStatus.OK).body(panels);
//    }

    @GetMapping("/boards/{boardId}/panels/archived")
    @Operation(summary = "Getting all archived board panels")
    public ResponseEntity<List<Panel>> getArchivedPanelsByBoardId(@PathVariable Long boardId) {
        List<Panel> panels = panelService.getArchivedPanels(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(panels);
    }

    @GetMapping("/boards/{boardId}/panels/non-archived")
    @Operation(summary = "Getting all active board panels")
    public ResponseEntity<List<Panel>> getNonArchivedPanelsByBoardId(@PathVariable Long boardId) {
        List<Panel> panels = panelService.getNonArchivedPanels(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(panels);
    }

}
