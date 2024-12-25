package ru.java.teamProject.SmartTaskFlow.service.abstr;

import ru.java.teamProject.SmartTaskFlow.dto.board.BoardDTO;
import ru.java.teamProject.SmartTaskFlow.dto.board.BoardPreviewDto;
import ru.java.teamProject.SmartTaskFlow.dto.board.CreateBoardDTO;
import ru.java.teamProject.SmartTaskFlow.dto.board.UpdateBoardDTO;
import ru.java.teamProject.SmartTaskFlow.entity.Board;

import java.util.List;

public interface BoardService {
    BoardDTO createBoard( CreateBoardDTO boardDTO);
    BoardPreviewDto updateBoard(Long boardId, UpdateBoardDTO boardDTO);
    void deleteBoard(Long boardId);
    BoardDTO addMember(Long boardId, String usernameOrEmail);
    List<BoardDTO> getAllBoards(String email);
    BoardDTO findById(Long id);
    Board archiveBoard(Long id);
    Board unArchiveBoard(Long id);
    List<BoardPreviewDto> getArchivedBoards();
    List<BoardPreviewDto> getNonArchivedBoards();
    Board getArchivedBoardById(Long id);
    Board getBoardById(Long id);

}
