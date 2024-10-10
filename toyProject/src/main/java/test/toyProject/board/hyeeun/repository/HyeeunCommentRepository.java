package test.toyProject.board.hyeeun.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.toyProject.board.hyeeun.entity.HyeeunBoardEntity;
import test.toyProject.board.hyeeun.entity.HyeeunCommentEntity;

import java.util.List;

public interface HyeeunCommentRepository extends JpaRepository<HyeeunCommentEntity, Long> {
    // 쿼리를 위한 정의
    // select * from comment_table where board_id=? order by id desc;
    // select * from comment_table where board_id=? -> List<CommentEntity> findAllByBoardEntity
    // order by id desc; -> OrderByIdDesc
    // board_id -> BoardEntity
    List<HyeeunCommentEntity> findAllByBoardEntityOrderByIdDesc(HyeeunBoardEntity boardEntity);
}
