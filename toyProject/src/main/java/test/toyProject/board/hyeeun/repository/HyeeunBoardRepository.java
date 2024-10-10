package test.toyProject.board.hyeeun.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import test.toyProject.board.hyeeun.entity.HyeeunBoardEntity;

public interface HyeeunBoardRepository extends JpaRepository<HyeeunBoardEntity, Long> {
    // update board_table set board_hits=board_hits+1 where id=? 기존의 조회수에서 하나를 증가시켜서 조회수값으로 바꿈, 단, 해당 게시물에 한해서만
    @Modifying // update나 delete 쿼리를 실행해야 할때 필수로 작성해야
    @Query(value = "update HyeeunBoardEntity b set b.boardHits=b.boardHits+1 where b.id=:id")
    void updateHits(@Param("id") Long id); // entity 기준 쿼리, b와 같은 약어로 쓰는게 원칙

    @Query("SELECT COUNT(b.id) FROM YunaBoardEntity b")
    int getTotalPostCount();
}
