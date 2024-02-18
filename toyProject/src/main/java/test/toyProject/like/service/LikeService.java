package test.toyProject.like.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import test.toyProject.board.seoyun.entity.SeoyunBoardEntity;
import test.toyProject.board.seoyun.repository.SeoyunBoardRepository;
import test.toyProject.like.entity.LikeEntity;
import test.toyProject.like.repository.LikeRepository;
import test.toyProject.user.entity.UserEntity;
import test.toyProject.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class LikeService {


    private final SeoyunBoardRepository boardRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;


    public ResponseEntity<?> likeSeoyunPost(Long postId, Long userId) {
        SeoyunBoardEntity post = boardRepository.findById(postId).orElse(null);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }

        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        // 이미 좋아요를 눌렀는지 확인
        LikeEntity existingLike = likeRepository.findBySeoyunBoardEntityAndUserEntity(post, user);
        if (existingLike != null) {
            return ResponseEntity.badRequest().body("이미 좋아요를 눌렀습니다.");
        }

        // 좋아요 엔티티 생성 및 저장
        LikeEntity like = new LikeEntity();
        like.setSeoyunBoardEntity(post);
        like.setUserEntity(user);
        likeRepository.save(like);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> cancelSeoyunPostLike(Long postId, Long userId) {
        SeoyunBoardEntity post = boardRepository.findById(postId).orElse(null);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }

        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        // 해당 게시물에 대한 사용자의 좋아요 찾기
        LikeEntity existingLike = likeRepository.findBySeoyunBoardEntityAndUserEntity(post, user);
        if (existingLike == null) {
            return ResponseEntity.badRequest().body("해당 게시물에 대한 좋아요가 없습니다.");
        }

        // 좋아요 취소
        likeRepository.delete(existingLike);

        return ResponseEntity.ok().build();
    }

    public boolean isPostLikedByUser(Long postId, Long userId) {
        return likeRepository.existsBySeoyunBoardEntity_IdAndUserEntity_Id(postId, userId);
    }
}
