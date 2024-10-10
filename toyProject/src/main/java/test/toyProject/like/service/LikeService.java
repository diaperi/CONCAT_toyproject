package test.toyProject.like.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import test.toyProject.board.hyeeun.entity.HyeeunBoardEntity;
import test.toyProject.board.hyeeun.repository.HyeeunBoardRepository;
import test.toyProject.board.seoyun.entity.SeoyunBoardEntity;
import test.toyProject.board.seoyun.repository.SeoyunBoardRepository;
import test.toyProject.board.yoonseo.entity.YoonseoBoardEntity;
import test.toyProject.board.yoonseo.repository.YoonseoBoardRepository;
import test.toyProject.board.yuna.entity.YunaBoardEntity;
import test.toyProject.board.yuna.repository.YunaBoardRepository;
import test.toyProject.like.entity.LikeEntity;
import test.toyProject.like.repository.LikeRepository;
import test.toyProject.user.entity.UserEntity;
import test.toyProject.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class LikeService {


    private final SeoyunBoardRepository boardRepository;
    private final HyeeunBoardRepository hyeeunboardRepository;
    private final YoonseoBoardRepository yoonseoBoardRepository;
    private final YunaBoardRepository yunaBoardRepository;
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



    //**********************혜은***************************
    public ResponseEntity<?> likeHyeeunPost(Long postId, Long userId) {
        HyeeunBoardEntity post = hyeeunboardRepository.findById(postId).orElse(null);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }

        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        // 이미 좋아요를 눌렀는지 확인
        LikeEntity existingLike = likeRepository.findByHyeeunBoardEntityAndUserEntity(post, user);
        if (existingLike != null) {
            return ResponseEntity.badRequest().body("이미 좋아요를 눌렀습니다.");
        }


        // 좋아요 엔티티 생성 및 저장
        LikeEntity like = new LikeEntity();
        like.setHyeeunBoardEntity(post);
        like.setUserEntity(user);
        likeRepository.save(like);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> cancelHyeeunPostLike(Long postId, Long userId) {
        HyeeunBoardEntity post = hyeeunboardRepository.findById(postId).orElse(null);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }

        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        // 해당 게시물에 대한 사용자의 좋아요 찾기
        LikeEntity existingLike = likeRepository.findByHyeeunBoardEntityAndUserEntity(post, user);
        if (existingLike == null) {
            return ResponseEntity.badRequest().body("해당 게시물에 대한 좋아요가 없습니다.");
        }

        // 좋아요 취소
        likeRepository.delete(existingLike);

        return ResponseEntity.ok().build();
    }

    public boolean HyeeunisPostLikedByUser(Long postId, Long userId) {
        return likeRepository.existsByHyeeunBoardEntity_IdAndUserEntity_Id(postId, userId);
    }




    //**********************윤서***************************
    public ResponseEntity<?> likeYoonseoPost(Long postId, Long userId) {
        YoonseoBoardEntity post = yoonseoBoardRepository.findById(postId).orElse(null);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }

        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        // 이미 좋아요를 눌렀는지 확인
        LikeEntity existingLike = likeRepository.findByYoonseoBoardEntityAndUserEntity(post, user);
        if (existingLike != null) {
            return ResponseEntity.badRequest().body("이미 좋아요를 눌렀습니다.");
        }


        // 좋아요 엔티티 생성 및 저장
        LikeEntity like = new LikeEntity();
        like.setYoonseoBoardEntity(post);
        like.setUserEntity(user);
        likeRepository.save(like);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> cancelYoonseoPostLike(Long postId, Long userId) {
        YoonseoBoardEntity post = yoonseoBoardRepository.findById(postId).orElse(null);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }

        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        // 해당 게시물에 대한 사용자의 좋아요 찾기
        LikeEntity existingLike = likeRepository.findByYoonseoBoardEntityAndUserEntity(post, user);
        if (existingLike == null) {
            return ResponseEntity.badRequest().body("해당 게시물에 대한 좋아요가 없습니다.");
        }

        // 좋아요 취소
        likeRepository.delete(existingLike);

        return ResponseEntity.ok().build();
    }

    public boolean YoonseoisPostLikedByUser(Long postId, Long userId) {
        return likeRepository.existsByYoonseoBoardEntity_IdAndUserEntity_Id(postId, userId);
    }

    public void deleteByYoonseoBoardId(Long id) {
        likeRepository.deleteByYoonseoBoardEntity_Id(id);
    }




    //*************************윤아*******************************
    public ResponseEntity<?> likeYunaPost(Long postId, Long userId) {
        YunaBoardEntity post = yunaBoardRepository.findById(postId).orElse(null);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }

        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        // 이미 좋아요를 눌렀는지 확인
        LikeEntity existingLike = likeRepository.findByYunaBoardEntityAndUserEntity(post, user);
        if (existingLike != null) {
            return ResponseEntity.badRequest().body("이미 좋아요를 눌렀습니다.");
        }

        // 좋아요 엔티티 생성 및 저장
        LikeEntity like = new LikeEntity();
        like.setYunaBoardEntity(post);
        like.setUserEntity(user);
        likeRepository.save(like);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> cancelYunaPostLike(Long postId, Long userId) {
        YunaBoardEntity post = yunaBoardRepository.findById(postId).orElse(null);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }

        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        // 해당 게시물에 대한 사용자의 좋아요 찾기
        LikeEntity existingLike = likeRepository.findByYunaBoardEntityAndUserEntity(post, user);
        if (existingLike == null) {
            return ResponseEntity.badRequest().body("해당 게시물에 대한 좋아요가 없습니다.");
        }

        // 좋아요 취소
        likeRepository.delete(existingLike);

        return ResponseEntity.ok().build();
    }

    public boolean YunaisPostLikedByUser(Long postId, Long userId) {
        return likeRepository.existsByYunaBoardEntity_IdAndUserEntity_Id(postId, userId);
    }
}
