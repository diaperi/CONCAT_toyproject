package test.toyProject.board.seoyun.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.toyProject.board.seoyun.entity.SeoyunBoardEntity;
import test.toyProject.board.seoyun.entity.SeoyunCommentEntity;

import java.util.List;

public interface SeoyunCommentRepository extends JpaRepository<SeoyunCommentEntity, Long> {
    List<SeoyunCommentEntity> findAllByBoardEntityOrderByIdDesc(SeoyunBoardEntity boardEntity);
}
