package test.toyProject.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.toyProject.board.hyeeun.entity.HyeeunBoardEntity;
import test.toyProject.board.seoyun.entity.SeoyunBoardEntity;
import test.toyProject.board.yoonseo.entity.YoonseoBoardEntity;
import test.toyProject.board.yuna.entity.YunaBoardEntity;
import test.toyProject.like.entity.LikeEntity;
import test.toyProject.user.entity.UserEntity;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    LikeEntity findBySeoyunBoardEntityAndUserEntity(SeoyunBoardEntity post, UserEntity user);
    LikeEntity findByHyeeunBoardEntityAndUserEntity(HyeeunBoardEntity post, UserEntity user);
    LikeEntity findByYoonseoBoardEntityAndUserEntity(YoonseoBoardEntity post, UserEntity user);
    LikeEntity findByYunaBoardEntityAndUserEntity(YunaBoardEntity post, UserEntity user);

    boolean existsBySeoyunBoardEntity_IdAndUserEntity_Id(Long postId, Long userId);
    boolean existsByHyeeunBoardEntity_IdAndUserEntity_Id(Long postId, Long userId);
    boolean existsByYoonseoBoardEntity_IdAndUserEntity_Id(Long postId, Long userId);
    boolean existsByYunaBoardEntity_IdAndUserEntity_Id(Long postId, Long userId);

    void deleteByYoonseoBoardEntity_Id(Long id);
}
