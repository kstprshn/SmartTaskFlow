package ru.java.teamProject.SmartTaskFlow.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.java.teamProject.SmartTaskFlow.dto.board.BoardDTO;
import ru.java.teamProject.SmartTaskFlow.dto.board.BoardPreviewDto;
import ru.java.teamProject.SmartTaskFlow.dto.board.CreateBoardDTO;
import ru.java.teamProject.SmartTaskFlow.dto.board.UpdateBoardDTO;
import ru.java.teamProject.SmartTaskFlow.dto.user.UserPreviewDTO;
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
                .setDescription(board.getDescription())
                .setMembers(board.getMembers().stream().map(User::getId).collect(Collectors.toList()));
    }

    private BoardPreviewDto buildPreviewDto(Board board){
        return new BoardPreviewDto()
                .setName(board.getName());
    }
    @Override
    public BoardDTO createBoard(CreateBoardDTO boardDTO, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(EntityNotFoundException::new);

        Board board = new Board();
        board.getMembers().add(user);
        board.setName(boardDTO.getName());
        board.setDescription(boardDTO.getDescription());

        boardRepository.save(board);

        log.info("Creating a board with the name {}", boardDTO.getName());
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
    public List<BoardDTO> getBoardsForUser(String email) {
        log.info("Fetching all boards for user: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return boardRepository.findByMembersContaining(user)
                .stream()
                .map(this::buildDto).toList();
    }

    @Override
    public Board archiveBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));

        board.setArchived(true);

        log.info("A board with the name {} is archived", board.getName());
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
    public List<BoardDTO> getArchivedBoards(Authentication authentication) {
        List<Board> archivedBoards = boardRepository.findAllByArchivedTrue();
        return archivedBoards.stream()
                .map(this::buildDto)
                .collect(Collectors.toList());
    }
    @Override
    public List<BoardDTO> getNonArchivedBoards(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("User not found"));

        List<Board> boards = boardRepository.findAllByMembersContainsAndArchivedFalse(user);

        return boards.stream()
                .map(this::buildDto)
                .collect(Collectors.toList());
    }
    @Override
    public Board getArchivedBoardById(Long id) {
        return boardRepository.findByIdAndArchivedTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Archived board not found"));
    }

    @Override
    public Board findBoardById(Long id) {

        log.info("Fetching an active board with id {}", id);
        return boardRepository.findByIdAndArchivedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));
    }

    @Override
    public List<UserPreviewDTO> getBoardMembers(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));
        return board.getMembers()
                .stream()
                .map(user -> new UserPreviewDTO()
                        .setFirstName(user.getFirstName()))
                .collect(Collectors.toList());
    }
}



