package ru.java.teamProject.SmartTaskFlow.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.java.teamProject.SmartTaskFlow.dto.board.BoardDTO;
import ru.java.teamProject.SmartTaskFlow.dto.board.BoardPreviewDto;
import ru.java.teamProject.SmartTaskFlow.dto.board.CreateBoardDTO;
import ru.java.teamProject.SmartTaskFlow.dto.board.UpdateBoardDTO;
import ru.java.teamProject.SmartTaskFlow.dto.user.UserPreviewDTO;
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
    public ResponseEntity<?> createBoard( @Valid @RequestBody CreateBoardDTO boardDTO) {
        return ResponseEntity.ok(boardService.createBoard(boardDTO));
    }

    @PatchMapping("/{boardId}/edit")
    public ResponseEntity<BoardPreviewDto> updateBoard(@PathVariable Long boardId, @Valid @RequestBody UpdateBoardDTO boardDTO) {
        return ResponseEntity.ok(boardService.updateBoard(boardId, boardDTO));
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.ok("Board deleted successfully.");
    }

    @PutMapping("/{boardId}/add-member")
    public ResponseEntity<?> addMemberToBoard(@PathVariable Long boardId, @RequestBody String usernameOrEmail) {
        return ResponseEntity.ok(boardService.addMember(boardId, usernameOrEmail));
    }

    @GetMapping("/userBoards")
    public ResponseEntity<List<BoardDTO>> getUserAllBoards(Authentication authentication) {
        String email = authentication.getName();
        List<BoardDTO> boards = boardService.getAllBoards(email);
        return ResponseEntity.ok(boards);
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
    public ResponseEntity<List<BoardPreviewDto>> getArchivedBoards() {
        return ResponseEntity.ok(boardService.getArchivedBoards());
    }

    @GetMapping("/non-archived")
    public ResponseEntity<List<BoardPreviewDto>> getNonArchivedBoards() {
        return ResponseEntity.ok(boardService.getNonArchivedBoards());
    }

    @GetMapping("/{id}/archived")
    public ResponseEntity<Board> getArchivedBoardById(@PathVariable Long id) {
        return ResponseEntity.ok(boardService.getArchivedBoardById(id));
    }
    @GetMapping("/{id}/getBoard")
    public ResponseEntity<Board> getBoardById(@PathVariable Long id) {
        return ResponseEntity.ok(boardService.findBoardById(id));
    }
    @GetMapping("/{boardId}/getMembers")
    public ResponseEntity<List<UserPreviewDTO>> getUsersInTask(@PathVariable Long boardId) {
        return ResponseEntity.ok(boardService.getUsersInBoard(boardId));
    }
}


