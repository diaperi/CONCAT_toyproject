package test.toyProject.board.yoonseo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import test.toyProject.board.yoonseo.entity.YoonseoBoardEntity;

public interface YoonseoBoardRepository extends JpaRepository<YoonseoBoardEntity,Long> {
    @Modifying

    @Query(value = "update YoonseoBoardEntity b set b.boardHits=b.boardHits+1 where b.id=:id")
    void updateHits(@Param("id") Long id);

    @Query("SELECT COUNT(b.id) FROM YoonseoBoardEntity b")
    int getTotalPostCount();
}
