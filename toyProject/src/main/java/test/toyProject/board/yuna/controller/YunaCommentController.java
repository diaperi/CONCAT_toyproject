package test.toyProject.board.yuna.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import test.toyProject.board.yuna.dto.YunaCommentDTO;
import test.toyProject.board.yuna.service.YunaCommentService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/yuna/comment")
public class YunaCommentController {
    private final YunaCommentService commentService;
    @PostMapping("/save")
    public ResponseEntity save(@ModelAttribute YunaCommentDTO commentDTO) {
        System.out.println("commentDTO = " + commentDTO);
        Long saveResult = commentService.save(commentDTO);
        if (saveResult != null) {
            List<YunaCommentDTO> commentDTOList = commentService.findAll(commentDTO.getBoardId());
            return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("해당 게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
    }
}
