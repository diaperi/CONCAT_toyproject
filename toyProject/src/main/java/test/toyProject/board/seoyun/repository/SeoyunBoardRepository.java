package test.toyProject.board.seoyun.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import test.toyProject.board.seoyun.entity.SeoyunBoardEntity;

import java.util.List;

public interface SeoyunBoardRepository extends JpaRepository<SeoyunBoardEntity, Long> {

    // 게시물 엔터티의 수를 세는 메서드
    @Query("SELECT COUNT(b.id) FROM SeoyunBoardEntity b")
    int getTotalPostCount();

//    @Query("SELECT b FROM SeoyunBoardEntity b ORDER BY b.id DESC")
//    List<SeoyunBoardEntity> findAllOrderedByIdDesc();

}