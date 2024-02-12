package test.toyProject.board.yuna.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.toyProject.board.yuna.entity.BoardEntity;
import test.toyProject.board.yuna.entity.CommentEntity;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findAllByBoardEntityOrderByIdDesc(BoardEntity boardEntity);
}
