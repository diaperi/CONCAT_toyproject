package test.toyProject.board.yuna.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import test.toyProject.board.yuna.entity.YunaBoardEntity;

public interface YunaBoardRepository extends JpaRepository<YunaBoardEntity, Long> {
    @Modifying
    @Query(value = "update YunaBoardEntity b set b.boardHits=b.boardHits+1 where b.id=:id")
    void updateHits(@Param("id") Long id);

    // 게시물 엔터티의 수를 세는 메서드
    @Query("SELECT COUNT(b.id) FROM YunaBoardEntity b")
    int getTotalPostCount();
}
