package test.toyProject.board.yoonseo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import test.toyProject.board.yoonseo.dto.YoonseoCommentDTO;
import test.toyProject.board.yoonseo.service.YoonseoCommentService;
import test.toyProject.user.dto.UserDTO;


import java.util.List;
@Controller
@RequiredArgsConstructor
@RequestMapping("/yoonseo/comment")
public class YoonseoCommentController {
    private final YoonseoCommentService commentService;

    @PostMapping("/save")
    public ResponseEntity save(@ModelAttribute YoonseoCommentDTO commentDTO, HttpServletRequest request, HttpSession session){
        UserDTO loggedInUser = (UserDTO) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            commentDTO.setCommentWriter(loggedInUser.getFullName());
            System.out.println("commentDTO = " + commentDTO);
            Long saveResult = commentService.save(commentDTO);
            if (saveResult != null) {
                List<YoonseoCommentDTO> commentDTOList = commentService.findAll(commentDTO.getBoardId());
                return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("해당 게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
        }
    }
    @GetMapping("/getByBoardId/{boardId}")
    public ResponseEntity<List<YoonseoCommentDTO>>getCommentByBoardId(@PathVariable Long boardId) {
        List<YoonseoCommentDTO> commentDTOList = commentService.findAll(boardId);
        if (commentDTOList != null) {
            return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
