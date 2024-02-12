package test.toyProject.board.yoonseo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import test.toyProject.board.yoonseo.dto.BoardDTO;
import test.toyProject.board.yoonseo.service.BoardService;


@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class YoonseoBoardController {

    // 각자 게시판 페이지로 이동
    @GetMapping("/yoonseoBoard")
    public String yoonseoBoard() {
        return "yoonseo/yoonseoBoard";
    }


}

