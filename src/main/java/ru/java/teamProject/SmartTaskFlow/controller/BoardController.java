package ru.java.teamProject.SmartTaskFlow.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.java.teamProject.SmartTaskFlow.dto.board.*;
import ru.java.teamProject.SmartTaskFlow.dto.user.UserPreviewDTO;
import ru.java.teamProject.SmartTaskFlow.entity.Board;
import ru.java.teamProject.SmartTaskFlow.service.abstr.BoardService;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
@Tag(name = "Board Api", description = "Methods for working with board API")

public class BoardController {

    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping("/add")
    @Operation(summary = "Creating a new board")
    public ResponseEntity<?> createBoard( @Valid @RequestBody CreateBoardDTO boardDTO, Authentication authentication) {
        return ResponseEntity.ok(boardService.createBoard(boardDTO, authentication));
    }
    

    @PatchMapping("/{boardId}/edit")
    @Operation(summary = "Updating the board")
    public ResponseEntity<BoardPreviewDto> updateBoard(@PathVariable Long boardId, @Valid @RequestBody UpdateBoardDTO boardDTO) {
        return ResponseEntity.ok(boardService.updateBoard(boardId, boardDTO));
    }

    @DeleteMapping("/{boardId}/delete")
    @Operation(summary = "Deleting the board")
    public ResponseEntity<?> deleteBoard(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.ok("Board deleted successfully.");
    }

    @PutMapping("/{boardId}/add-member")
    @Operation(summary = "Adding participants to the board")
    public ResponseEntity<?> addMemberToBoard(@PathVariable Long boardId, @RequestBody AddMemberDto addMemberDto) {
        return ResponseEntity.ok(boardService.addMember(boardId, addMemberDto.getUsernameOrEmail()));
    }

    @GetMapping("/userBoards/get")
    @Operation(summary = "Viewing the participant's boards")
    public ResponseEntity<List<BoardDTO>> getUserAllBoards(Authentication authentication) {
        String email = authentication.getName();
        List<BoardDTO> boards = boardService.getBoardsForUser(email);
        return ResponseEntity.ok(boards);
    }

    @PatchMapping("/{id}/archive")
    @Operation(summary = "Archiving the board")
    public ResponseEntity<Board> archiveBoard(@PathVariable Long id) {
        Board archivedBoard = boardService.archiveBoard(id);
        return ResponseEntity.ok(archivedBoard);
    }

    @PatchMapping("/{id}/unarchive")
    @Operation(summary = "Unarchiving the board")
    public ResponseEntity<Board> unArchiveBoard(@PathVariable Long id) {
        Board unArchivedBoard = boardService.unArchiveBoard(id);
        return ResponseEntity.ok(unArchivedBoard);
    }

    @GetMapping("/archived")
    @Operation(summary = "Getting all archived boards")
    public ResponseEntity<?> getArchivedBoards(Authentication authentication) {
        return ResponseEntity.ok(boardService.getArchivedBoards(authentication));
    }

    @GetMapping("/non-archived")
    @Operation(summary = "Getting active boards")
    public ResponseEntity<?> getNonArchivedBoards(Authentication authentication) {
        return ResponseEntity.ok(boardService.getNonArchivedBoards(authentication));
    }

    @GetMapping("/{id}/archived")
    @Operation(summary = "Getting an archived board")
    public ResponseEntity<Board> getArchivedBoardById(@PathVariable Long id) {
        return ResponseEntity.ok(boardService.getArchivedBoardById(id));
    }
//    @GetMapping("/{id}/getBoard")
//    public ResponseEntity<Board> getBoardById(@PathVariable Long id) {
//        return ResponseEntity.ok(boardService.findBoardById(id));
//    }
    @GetMapping("/{boardId}/getMembers")
    @Operation(summary = "Viewing task members in the board")
    public ResponseEntity<List<UserPreviewDTO>> getUsersInTask(@PathVariable Long boardId) {
        return ResponseEntity.ok(boardService.getBoardMembers(boardId));
    }
}


