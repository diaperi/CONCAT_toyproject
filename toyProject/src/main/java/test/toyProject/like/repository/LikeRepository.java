package test.toyProject.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.toyProject.board.seoyun.entity.SeoyunBoardEntity;
import test.toyProject.like.entity.LikeEntity;
import test.toyProject.user.entity.UserEntity;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    LikeEntity findBySeoyunBoardEntityAndUserEntity(SeoyunBoardEntity post, UserEntity user);

    boolean existsBySeoyunBoardEntity_IdAndUserEntity_Id(Long postId, Long userId);
}
