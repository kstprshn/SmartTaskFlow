package ru.java.teamProject.SmartTaskFlow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.java.teamProject.SmartTaskFlow.entity.Board;
import ru.java.teamProject.SmartTaskFlow.entity.User;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByMembersContaining(User user);
    List<Board> findAllByArchivedTrue();
    List<Board> findAllByMembersContainsAndArchivedFalse(User user);
    //List<Board> findAllByUserAndArchivedFalse(List<User> users);

    List<Board> findAllByArchivedFalse();


    Optional<Board> findByIdAndArchivedTrue(Long id);
    Optional<Board> findByIdAndArchivedFalse(Long id);

}