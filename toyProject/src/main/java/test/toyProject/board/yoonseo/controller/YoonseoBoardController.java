package test.toyProject.board.yoonseo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import test.toyProject.board.yoonseo.dto.YoonseoBoardDTO;
import test.toyProject.board.yoonseo.service.YoonseoBoardService;


import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board/yoonseo") // 작성 시 헷갈렸던 부분. return부분을 작성할 때 html위치.
public class YoonseoBoardController {
    private final YoonseoBoardService boardService;

    // 각자 게시판 페이지로 이동
    @GetMapping("/yoonseoBoard")
    public String yoonseoBoard() {
        return "/board/yoonseo/yoonseoBoard";
    }

    @GetMapping("/save")
    public String saveFrom(){
        return "/board/yoonseo/save";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute YoonseoBoardDTO boardDTO) throws IOException { //추가
        System.out.println("boardDTO=" + boardDTO);
        boardService.save(boardDTO);
        return "/board/yoonseo/yoonseoBoard"; // 전에 만들었던거에서는 index였지만 지금은 index를 yoonseoBoard로 했기에 이걸로 대체
    }
    //글 목록부분
    @GetMapping("/")
    public String findAll(Model model) {
        List<YoonseoBoardDTO> boardDTOList = boardService.findAll();
        model.addAttribute("boardList", boardDTOList);
        return "/yoonseo/list"; //list에서 -> yoonseo추가 했더니 list.html로 연결됨.
    }
    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model,@PageableDefault(page=1) Pageable pageable) {
        boardService.updateHits(id); //조회 수는 올라감, 근데 detail페이지가 안뜨는게 문제, board/1 -> 해결완료!
        YoonseoBoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);
        model.addAttribute("page", pageable.getPageNumber());

        return "yoonseo/detail";
    }
    //글 수정
    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        YoonseoBoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("boardUpdate", boardDTO);
        return "/yoonseo/update"; //경로 설정했더니 수정됨!
    }

    @PostMapping("/update")
    public String update(@ModelAttribute YoonseoBoardDTO boardDTO, Model model) {
        YoonseoBoardDTO board = boardService.update(boardDTO);
        model.addAttribute("board", board);
        return "/yoonseo/detail"; //수정후 목록 조회시 페이지가 null값이라고 나오는 문제발생.


    }
    //삭제
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        boardService.delete(id);
        return "redirect:/board/";
    }

    //페이징부분
    //오류 사항: 게시글을 수정하고 나서 페이지목록 조회시 조회가 안되고 page=null이 됨...
    @GetMapping("/paging")
    public String paging(@PageableDefault(page = 1) Pageable pageable, Model model) {
        Page<YoonseoBoardDTO> boardList = boardService.paging(pageable);
        int blockLimit = 3;  //화면 밑에 보여지는 페이지번호 갯수
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
        // 다음 페이지로 넘어갈 때에는 마지막 페이지번호 갯수에 1을 더한 값이 시작페이지 값이 될 수있도록 해야하기에 blockLimit + 1을 해주는 것!
        int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : boardList.getTotalPages();
        model.addAttribute("boardList", boardList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "/board/yoonseo/paging"; //list와 마찬가지로!

    }


}
