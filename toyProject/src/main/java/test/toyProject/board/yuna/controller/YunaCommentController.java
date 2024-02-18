package test.toyProject.board.yuna.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import test.toyProject.board.yuna.dto.YunaBoardDTO;
import test.toyProject.board.yuna.dto.YunaCommentDTO;
import test.toyProject.board.yuna.service.YunaCommentService;
import test.toyProject.user.dto.UserDTO;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/yuna/comment")
public class YunaCommentController {
    private final YunaCommentService commentService;

    // 댓글 작성, 저장
    @PostMapping("/save")
    public ResponseEntity save(@ModelAttribute YunaCommentDTO commentDTO, HttpServletRequest request, HttpSession session) {
        UserDTO loggedInUser = (UserDTO) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            commentDTO.setCommentWriter(loggedInUser.getFullName());

            System.out.println("commentDTO = " + commentDTO);
            Long saveResult = commentService.save(commentDTO);

            if (saveResult != null) {
                List<YunaCommentDTO> commentDTOList = commentService.findAll(commentDTO.getBoardId());
                return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("해당 게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
        }
    }
    // 댓글 목록
    @GetMapping("/getByBoardId/{boardId}")
    public ResponseEntity<List<YunaCommentDTO>>getCommentByBoardId(@PathVariable Long boardId) {
        List<YunaCommentDTO> commentDTOList = commentService.findAll(boardId);
        if (commentDTOList != null) {
            return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}