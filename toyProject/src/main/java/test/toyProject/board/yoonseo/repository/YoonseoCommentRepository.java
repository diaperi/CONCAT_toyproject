package test.toyProject.board.yoonseo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.toyProject.board.yoonseo.entity.YoonseoBoardEntity;
import test.toyProject.board.yoonseo.entity.YoonseoCommentEntity;

import java.util.List;

public interface YoonseoCommentRepository extends JpaRepository <YoonseoCommentEntity,Long> {
    List<YoonseoCommentEntity> findAllByBoardEntityOrderByIdDesc(YoonseoBoardEntity boardEntity);

    void deleteByBoardEntity_Id(Long id);
}
