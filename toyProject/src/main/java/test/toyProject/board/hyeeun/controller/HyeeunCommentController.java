package test.toyProject.board.hyeeun.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import test.toyProject.board.hyeeun.dto.HyeeunCommentDTO;
import test.toyProject.board.hyeeun.service.HyeeunCommentService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class HyeeunCommentController {

    private final HyeeunCommentService commentService; // CommentService 주입받음

    @PostMapping("/save") // save 주소가 왔을 때
    public ResponseEntity save(@ModelAttribute HyeeunCommentDTO commentDTO) {
        System.out.println("commentDTO = " + commentDTO);
        Long saveResult = commentService.save(commentDTO);// 서비스 클래스의 save 메서드 호출
        if (saveResult != null) {
            // 작성 성공하면 댓글목록을 가져와서 리턴
            // 댓글목록 : 해당 게시글의 댓글 전체
            List<HyeeunCommentDTO> commentDTOList = commentService.findAll(commentDTO.getBoardId());
            // entity : 바디와 헤더를 같이 다룰 수 있는 객체
            // commentDTOList : 내가 보내고자 하는 데이터 바디 값, HttpStatus.OK : 상태값
            return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
        } else {
            // ajax에서 error 부분 동작
            return new ResponseEntity("해당 게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }

    }
}
