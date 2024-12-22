package ru.java.teamProject.SmartTaskFlow.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.java.teamProject.SmartTaskFlow.dto.board.BoardDTO;
import ru.java.teamProject.SmartTaskFlow.dto.board.CreateBoardDTO;
import ru.java.teamProject.SmartTaskFlow.dto.board.UpdateBoardDTO;
import ru.java.teamProject.SmartTaskFlow.service.abstr.BoardService;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping
    public ResponseEntity<?> createBoard(Authentication authentication, @Valid @RequestBody CreateBoardDTO boardDTO) {
        String email = authentication.getName();
        return ResponseEntity.ok(boardService.createBoard(email, boardDTO));
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<?> updateBoardName(@PathVariable Long boardId, @Valid @RequestBody UpdateBoardDTO boardDTO) {
        return ResponseEntity.ok(boardService.updateBoardName(boardId, boardDTO));
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.ok("Board deleted successfully.");
    }

    @PutMapping("/{boardId}/members/{userId}")
    public ResponseEntity<?> addMember(@PathVariable Long boardId, @PathVariable Long userId) {
        return ResponseEntity.ok(boardService.addMember(boardId, userId));
    }

    @GetMapping
    public ResponseEntity<?> getAllBoards(Authentication authentication) {
        String email = authentication.getName();
        List<BoardDTO> boards = boardService.getAllBoards(email);
        return ResponseEntity.ok(boards);
    }
}


