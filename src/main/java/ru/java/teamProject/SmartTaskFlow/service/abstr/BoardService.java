package ru.java.teamProject.SmartTaskFlow.service.abstr;

import org.springframework.security.core.Authentication;
import ru.java.teamProject.SmartTaskFlow.dto.board.BoardDTO;
import ru.java.teamProject.SmartTaskFlow.dto.board.BoardPreviewDto;
import ru.java.teamProject.SmartTaskFlow.dto.board.CreateBoardDTO;
import ru.java.teamProject.SmartTaskFlow.dto.board.UpdateBoardDTO;
import ru.java.teamProject.SmartTaskFlow.dto.user.UserPreviewDTO;
import ru.java.teamProject.SmartTaskFlow.entity.Board;

import java.util.List;

public interface BoardService {
    BoardDTO createBoard( CreateBoardDTO boardDTO, Authentication authentication);
    BoardPreviewDto updateBoard(Long boardId, UpdateBoardDTO boardDTO);
    void deleteBoard(Long boardId);
    BoardDTO addMember(Long boardId, String usernameOrEmail);
    List<BoardDTO> getBoardsForUser(String email);
    Board archiveBoard(Long id);
    Board unArchiveBoard(Long id);
    List<BoardDTO> getArchivedBoards(Authentication authentication);
    List<BoardDTO> getNonArchivedBoards(Authentication authentication);
    Board getArchivedBoardById(Long id);
    Board findBoardById(Long id);
    List<UserPreviewDTO> getBoardMembers(Long boardId);


}
