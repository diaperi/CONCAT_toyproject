package test.toyProject.board.yuna.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.toyProject.board.yuna.entity.YunaBoardEntity;
import test.toyProject.board.yuna.entity.YunaCommentEntity;

import java.util.List;

public interface YunaCommentRepository extends JpaRepository<YunaCommentEntity, Long> {
    List<YunaCommentEntity> findAllByBoardEntityOrderByIdDesc(YunaBoardEntity boardEntity);
}
