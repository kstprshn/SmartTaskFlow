package ru.java.teamProject.SmartTaskFlow.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.java.teamProject.SmartTaskFlow.dto.board.BoardDTO;
import ru.java.teamProject.SmartTaskFlow.dto.board.CreateBoardDTO;
import ru.java.teamProject.SmartTaskFlow.dto.board.UpdateBoardDTO;
import ru.java.teamProject.SmartTaskFlow.entity.Board;
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

    @GetMapping("/all")
    public ResponseEntity<List<BoardDTO>> getAllBoards(Authentication authentication) {
        String email = authentication.getName();
        List<BoardDTO> boards = boardService.getAllBoards(email);
        return ResponseEntity.ok(boards);
    }

    @GetMapping("/getBoard/{boardId}")
    public ResponseEntity<BoardDTO> getBoard(@PathVariable Long boardId){
        BoardDTO boardById = boardService.findById(boardId);
        return new ResponseEntity<>(boardById, HttpStatus.OK);
    }

    @PatchMapping("/{id}/archive")
    public ResponseEntity<Board> archiveBoard(@PathVariable Long id) {
        Board archivedBoard = boardService.archiveBoard(id);
        return ResponseEntity.ok(archivedBoard);
    }

    @PatchMapping("/{id}/unarchive")
    public ResponseEntity<Board> unArchiveBoard(@PathVariable Long id) {
        Board unArchivedBoard = boardService.unArchiveBoard(id);
        return ResponseEntity.ok(unArchivedBoard);
    }

    @GetMapping("/archived")
    public ResponseEntity<List<Board>> getArchivedBoards() {
        return ResponseEntity.ok(boardService.getArchivedBoards());
    }

    @GetMapping("/non-archived")
    public ResponseEntity<List<Board>> getNonArchivedBoards() {
        return ResponseEntity.ok(boardService.getNonArchivedBoards());
    }

    @GetMapping("/archived/{id}")
    public ResponseEntity<Board> getArchivedBoardById(@PathVariable Long id) {
        return ResponseEntity.ok(boardService.getArchivedBoardById(id));
    }
    @GetMapping("/getBoard/{id}")
    public ResponseEntity<Board> getBoardById(@PathVariable Long id) {
        return ResponseEntity.ok(boardService.getBoardById(id));
    }
}


