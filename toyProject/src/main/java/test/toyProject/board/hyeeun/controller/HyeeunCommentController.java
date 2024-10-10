package test.toyProject.board.hyeeun.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import test.toyProject.board.hyeeun.dto.HyeeunCommentDTO;
import test.toyProject.board.hyeeun.service.HyeeunCommentService;
import test.toyProject.board.seoyun.dto.SeoyunCommentDTO;
import test.toyProject.user.dto.UserDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/hyeeun/comment")
public class HyeeunCommentController {

    private final HyeeunCommentService commentService; // CommentService 주입받음

    @PostMapping("/save")
    public ResponseEntity save(@ModelAttribute HyeeunCommentDTO commentDTO, HttpSession session) {
        UserDTO loggedInUser = (UserDTO) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            // 로그인한 사용자의 이름을 가져와서 DTO에 설정합니다.
            commentDTO.setCommentWriter(loggedInUser.getFullName());
            System.out.println("commentDTO = " + commentDTO);
            Long saveResult = commentService.save(commentDTO);
            if (saveResult != null) {
                List<HyeeunCommentDTO> commentDTOList = commentService.findAll(commentDTO.getBoardId());
                // 댓글 목록과 로그인 사용자 정보를 함께 반환
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("commentDTOList", commentDTOList);
                responseData.put("loggedInUser", loggedInUser.getFullName());

                return new ResponseEntity<>(responseData, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("해당 게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
            }
        } else {
            // 로그인한 사용자 정보가 세션에 없는 경우
            return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
        }
    }


    @GetMapping("/getByBoardId/{boardId}")
    public ResponseEntity<List<HyeeunCommentDTO>> getCommentsByBoardId(@PathVariable Long boardId) {
        List<HyeeunCommentDTO> commentDTOList = commentService.findAll(boardId);
        if (commentDTOList != null) {
            return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
