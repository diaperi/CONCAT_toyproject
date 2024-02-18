package test.toyProject.board.hyeeun.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import test.toyProject.board.hyeeun.dto.HyeeunBoardDTO;
import test.toyProject.board.hyeeun.dto.HyeeunCommentDTO;
import test.toyProject.board.hyeeun.service.HyeeunBoardService;
import test.toyProject.board.hyeeun.service.HyeeunCommentService;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board/hyeeun")
public class HyeeunBoardController {
    private final HyeeunBoardService boardService; // 생성자 주입 방식으로 의존성을 주입받도록
    private final HyeeunCommentService commentService; // CommentService 주입받음
    @GetMapping("/hyeeunBoard")
    public String hyeeunBoard(){
        return "hyeeun/hyeeunBoard";
    }

    @GetMapping("/save")
    public String saveForm() {
        return "/board/hyeeun/save";
    } // 그 이하의 주소를 각각의 메서드 중에서 맵핑 값이 일치하는 메서드 호출(get으로 받음)

    @PostMapping("/save")
    public String save(@ModelAttribute HyeeunBoardDTO boardDTO) throws IOException {
        System.out.println("boardDTO = " + boardDTO);
        boardService.save(boardDTO); // boardService.save 메서드 호출
        return "index";
    } // 그 이하의 주소를 각각의 메서드 중에서 맵핑 값이 일치하는 메서드 호출(post로 받음)

//    @GetMapping("/")
//    public String findAll(Model model) {
//        // DB에서 전체 게시글 데이터를 가져와서 list.html에 보여준다.
//        List<HyeeunBoardDTO> boardDTOList = boardService.findAll();
//        model.addAttribute("boardList", boardDTOList); // 가져온 데이터를 model 객체에 담기
//        return "list"; // list.html로 리턴
//    } // 전체 목록을 db로부터 가져와야함 -> model 객체 사용, 목록 여러개를 가져온다 -> List<BoardDTO> boardDTOList = boardService.findAll();

    @GetMapping("/{id}")
    // @PageableDefault(page=1) Pageable pageable : 페이지 요청이 없는 경우에 대한 것
    public String findById(@PathVariable Long id, Model model,
                           @PageableDefault(page=1) Pageable pageable) {
    /*
        해당 게시글의 조회수를 하나 올리고
        게시글 데이터를 가져와서 detail.html에 출력
    */
        boardService.updateHits(id); // 조회수 처리
        HyeeunBoardDTO boardDTO = boardService. findById(id); // 해당 게시글을 가져와서 dto로 받아오고
        /* 댓글 목록 가져오기 */
        List<HyeeunCommentDTO> commentDTOList = commentService.findAll(id);
        model.addAttribute("commentList", commentDTOList); // model에 담아서 detail로 넘어감

        model.addAttribute("board", boardDTO); // model 객체를 board라는 파라미터에 담아서 출력
        model.addAttribute("page", pageable.getPageNumber()); // pageable.getPageNumber() : 여기서 담아서 detail.html로 가져가기 위한 용도
        return "board/hyeeun/detail"; // detail.html로 넘어감
    } // pathvariable : 경로상에 있는 값을 가져올 때 사용

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        HyeeunBoardDTO boardDTO = boardService.findById(id); // 해당 게시글의 정보 가져오기
        model.addAttribute("boardUpdate", boardDTO);
        return "board/hyeeun/update"; // update.html로 넘어감
    } // pathvariable : 경로상에 있는 값을 가져올 때 사용 /  model : 데이터를 담아가기 위해서 필요

    @PostMapping("/update")
    public String update(@ModelAttribute HyeeunBoardDTO boardDTO, Model model) {
        HyeeunBoardDTO board = boardService.update(boardDTO); // update 메서드 호출
        model.addAttribute("board", board);
        return "board/hyeeun/detail";
    } // 수정 후 수정이 반영된 상세페이지 보여주기

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        boardService.delete(id); // 서비스의 delete 메서드 호출
        return "redirect:/board/hyeeun"; // 끝나면 redirect로 목록을 호출
    }

    // /board/paging?page=1
    @GetMapping("/paging")
    public String paging(@PageableDefault(page = 1)Pageable pageable, Model model) {
        //       pageable.getPageNumber();
        // 게시글을 페이징 처리해서 갖고옴 -> <BoardDTO>
        // 서비스에서 페이징이라는 메서드 호출을 해서 가져오겠다 -> boardService.paging(pageable)
        Page<HyeeunBoardDTO> boardList = boardService.paging(pageable);
        int blockLimit = 3; // 1 2 3

        // startPage : 현재 사용자가 1 또는 2 또는 3페이지에 있으면 1을 줌,
        // 현재 사용자가 7 또는 8 또는 9페이지에 있으면 7이라는 값을 만들어줌
        // (double)pageable.getPageNumber() / blockLimit))) - 1 : 현재 사용자가 요청한 페이지를 블럭 리밋으로 나눠서 소수점을 올리는 처리후 1을 뺌
        // blockLimit + 1 : 그 페이지 값을 곱해서 더하기 1
        int startPage = (((int) (Math.ceil((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10 ~~

        // endPage : 3, 6, 9,...
        // (startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : 만약에 9보다 실제 페이지 개수가 작은 경우, 9라는 값을 보여주지 말고 전체 페이지 값을 endPage 값으로 해라
        int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : boardList.getTotalPages();


        // page 개수 20개
        // 현재 사용자가 3페이지
        // 1 2 3
        // 현재 사용자가 7페이지
        // 7 8 9
        // 보여지는 페이지 개수 3개
        // 총 페이지 개수 8개

        model.addAttribute("boardList", boardList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "/board/hyeeun/paging"; // paging.html로 넘어감
    }

    }






