package test.toyProject.board.yoonseo.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import test.toyProject.board.yoonseo.dto.YoonseoBoardDTO;
import test.toyProject.board.yoonseo.dto.YoonseoCommentDTO;
import test.toyProject.board.yoonseo.service.YoonseoBoardService;
import test.toyProject.board.yoonseo.service.YoonseoCommentService;
import test.toyProject.user.dto.UserDTO;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board/yoonseo") // 작성 시 헷갈렸던 부분. return부분을 작성할 때 html위치.
public class YoonseoBoardController {
    private final YoonseoBoardService boardService;
    private final YoonseoCommentService commentService;


    // 각자 게시판 페이지로 이동
    @GetMapping("/yoonseoBoard")
    public String yoonseoBoard() {
        return "/board/yoonseo/yoonseoBoard";
    }

    @GetMapping("/save")
    public String saveFrom(HttpSession session, Model model){
        UserDTO loggedInUser = (UserDTO) session.getAttribute("loggedInUser");
        if(loggedInUser == null) {
            return "redirect:/user/login";
        }
        return "/board/yoonseo/save";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute YoonseoBoardDTO boardDTO, HttpSession session) throws IOException { //추가
        UserDTO loggedInUser = (UserDTO) session.getAttribute("loggedInUser");
        if(loggedInUser != null) {
            // 로그인한 사용자의 정보를 게시물 작성자로 설정
            String boardWriter = loggedInUser.getFullName();

            boardDTO.setBoardCreatedTime(LocalDateTime.now());

            boardDTO.setBoardWriter(boardWriter);
            System.out.println("boardDTO=" + boardDTO);
            boardService.save(boardDTO);
            return "/board/yoonseo/yoonseoBoard";
        }else {
            return "redirect:/user/login";
        }

    }
    //글 목록부분
    @GetMapping("/")
    public String findAll(Model model) {
        List<YoonseoBoardDTO> boardDTOList = boardService.findAll();
        model.addAttribute("boardList", boardDTOList);
        return "yoonseo/list"; //list에서 -> yoonseo추가 했더니 list.html로 연결됨.

    }

    //게시글 목록에서 전체 게시물 카운트
    @GetMapping("/listCount")
    @ResponseBody // JSON 형식으로 응답하기 위해 추가
    public ResponseEntity<Map<String, Integer>> getTotalPostCount() {
        int totalCount = boardService.getTotalPostCount();

        Map<String, Integer> response = new HashMap<>();
        response.put("totalCount", totalCount);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model,@PageableDefault(page=1) Pageable pageable) {
        boardService.updateHits(id); //조회 수는 올라감, 근데 detail페이지가 안뜨는게 문제, board/1 -> 해결완료!
        YoonseoBoardDTO boardDTO = boardService.findById(id);
        List<YoonseoCommentDTO> commentDTOList = commentService.findAll(id);
        //여기 commentServic오류
        model.addAttribute("commentList", commentDTOList); // model에 담아서 detail로 넘어감

        model.addAttribute("board", boardDTO);
        model.addAttribute("page", pageable.getPageNumber());

        return "/board/yoonseo/detail";
    }
    //글 수정
    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model,HttpSession session) {
        UserDTO loggedInUser = (UserDTO) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            YoonseoBoardDTO boardDTO = boardService.findById(id);
            if (boardDTO != null && boardDTO.getBoardWriter().equals(loggedInUser.getFullName())) {
                model.addAttribute("boardUpdate", boardDTO);
                model.addAttribute("loggedInUser", loggedInUser);
                return "/board/yoonseo/update";
            } else {
                return "redirect:/error";
            }
        } else {
            return "redirect:/user/login";
        }

    }

    @PostMapping("/update")
    public String update(@ModelAttribute YoonseoBoardDTO boardDTO, Model model,HttpSession session) {
        UserDTO loggedInUser = (UserDTO) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            YoonseoBoardDTO originalBoard = boardService.findById(boardDTO.getId());
            //수정
            if (originalBoard != null && originalBoard.getBoardWriter().equals(loggedInUser.getFullName())) {
                YoonseoBoardDTO board = boardService.update(boardDTO);
                model.addAttribute("board", board);
                return "redirect:/board/yoonseo/" + board.getId();
            } else {
                return "redirect:/error";
            }
        } else {
            return "redirect:/user/login";
        }

        //return "/board/yoonseo/detail"; //수정후 목록 조회시 페이지가 null값이라고 나오는 문제발생.

    }
    //삭제
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session) { ///역;
        UserDTO loggedInUser = (UserDTO) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            YoonseoBoardDTO boardDTO = boardService.findById(id);
            if (boardDTO != null && boardDTO.getBoardWriter().equals(loggedInUser.getFullName())) {
                boardService.delete(id);
                return "redirect:/board/yoonseo/list";
            } else {

                return "redirect:/error";
            }
        } else {
            return "redirect:/user/login";
        }

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
