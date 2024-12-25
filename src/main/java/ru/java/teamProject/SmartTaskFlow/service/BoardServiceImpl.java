package ru.java.teamProject.SmartTaskFlow.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.java.teamProject.SmartTaskFlow.dto.board.BoardDTO;
import ru.java.teamProject.SmartTaskFlow.dto.board.BoardPreviewDto;
import ru.java.teamProject.SmartTaskFlow.dto.board.CreateBoardDTO;
import ru.java.teamProject.SmartTaskFlow.dto.board.UpdateBoardDTO;
import ru.java.teamProject.SmartTaskFlow.entity.Board;
import ru.java.teamProject.SmartTaskFlow.entity.User;
import ru.java.teamProject.SmartTaskFlow.repository.BoardRepository;
import ru.java.teamProject.SmartTaskFlow.repository.UserRepository;
import ru.java.teamProject.SmartTaskFlow.service.abstr.BoardService;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@Slf4j
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public BoardServiceImpl(BoardRepository boardRepository, UserRepository userRepository) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
    }

    private BoardDTO buildDto(Board board){
        return new BoardDTO()
                .setId(board.getId())
                .setName(board.getName())
                .setArchived(board.getArchived())
                .setMembers(board.getMembers().stream().map(User::getId).collect(Collectors.toList()));
    }

    private BoardPreviewDto buildPreviewDto(Board board){
        return new BoardPreviewDto()
                .setName(board.getName());
    }
    @Override
    public BoardDTO createBoard(CreateBoardDTO boardDTO) {
        Board board = new Board();
        board.setName(boardDTO.getName());
        board.setDescription(boardDTO.getDescription());
        boardRepository.save(board);

        return buildDto(board);
    }

    @Override
    public BoardPreviewDto updateBoard(Long boardId, UpdateBoardDTO boardDTO) {
        log.info("Updating board name for board ID: {}", boardId);
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));
        board.setName(boardDTO.getName());
        board.setDescription(boardDTO.getDescription());
        boardRepository.save(board);
        return buildPreviewDto(board);
    }

    @Override
    public void deleteBoard(Long boardId) {
        log.info("Deleting board with ID: {}", boardId);
        boardRepository.deleteById(boardId);
    }

    @Override
    public BoardDTO addMember(Long boardId, String usernameOrEmail) {
        log.info("Adding member to board ID: {}", boardId);
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));
        User user = userRepository.findByUsernameOrEmailIgnoreCase(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (board.getMembers().contains(user)) {
            throw new IllegalArgumentException("User is already a member of this board");
        }
        board.getMembers().add(user);
        user.getBoards().add(board);

        boardRepository.save(board);
        userRepository.save(user);

        return buildDto(board);
    }

    @Override
    public List<BoardDTO> getAllBoards(String email) {
        log.info("Fetching all boards for user: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return boardRepository.findByMembersContaining(user)
                .stream()
                .map(this::buildDto).toList();
    }

    @Override
    public BoardDTO findById(Long id){
        Board foundedBoard =  boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("The board is not found"));
        return buildDto(foundedBoard);
    }

    @Override
    public Board archiveBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));

        board.setArchived(true);
        return boardRepository.save(board);
    }

    @Override
    public Board unArchiveBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));

        board.setArchived(false);
        return boardRepository.save(board);
    }
    @Override
    public List<BoardPreviewDto> getArchivedBoards() {
        List<Board> archivedBoards = boardRepository.findAllByArchivedTrue();
        return archivedBoards.stream()
                .map(this::buildPreviewDto)
                .collect(Collectors.toList());
    }
    @Override
    public List<BoardPreviewDto> getNonArchivedBoards() {
        List<Board> boards = boardRepository.findAllByArchivedFalse();
        return boards.stream()
                .map(this::buildPreviewDto)
                .collect(Collectors.toList());
    }
    @Override
    public Board getArchivedBoardById(Long id) {
        return boardRepository.findByIdAndArchivedTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Archived board not found"));
    }

    @Override
    public Board getBoardById(Long id) {
        return boardRepository.findByIdAndArchivedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));
    }
}



