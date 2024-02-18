package test.toyProject.like.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import test.toyProject.board.seoyun.entity.SeoyunBoardEntity;
import test.toyProject.board.seoyun.repository.SeoyunBoardRepository;
import test.toyProject.like.entity.LikeEntity;
import test.toyProject.like.repository.LikeRepository;
import test.toyProject.like.service.LikeService;
import test.toyProject.user.dto.UserDTO;
import test.toyProject.user.entity.UserEntity;
import test.toyProject.user.repository.UserRepository;

@Controller
@RequiredArgsConstructor
@RequestMapping("/like")
public class LikeController {

    private final SeoyunBoardRepository boardRepository;
    private final UserRepository userRepository;
    private final LikeService likeService;


    // 좋아요 기능
    @PostMapping("/seoyun/{postId}")
    public ResponseEntity<?> likeSeoyunPost(@PathVariable Long postId, HttpSession session) {
        UserDTO loggedInUser = (UserDTO) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        return likeService.likeSeoyunPost(postId, loggedInUser.getId());
    }


    // 좋아요 취소 기능
    @PostMapping("/seoyun/{postId}/cancel")
    public ResponseEntity<?> cancelSeoyunPostLike(@PathVariable Long postId, HttpSession session) {
        UserDTO loggedInUser = (UserDTO) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        return likeService.cancelSeoyunPostLike(postId, loggedInUser.getId());
    }


    @GetMapping("/seoyun/{postId}/isLiked")
    public ResponseEntity<Boolean> isPostLikedByUser(@PathVariable Long postId, HttpSession session) {
        UserDTO loggedInUser = (UserDTO) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.status(401).body(false); // 로그인되지 않은 경우
        }

        boolean isLiked = likeService.isPostLikedByUser(postId, loggedInUser.getId());
        return ResponseEntity.ok(isLiked);
    }
}
