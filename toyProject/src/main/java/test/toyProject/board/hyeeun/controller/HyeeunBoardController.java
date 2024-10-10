package test.toyProject.board.hyeeun.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import test.toyProject.board.hyeeun.dto.HyeeunBoardDTO;
import test.toyProject.board.hyeeun.dto.HyeeunCommentDTO;
import test.toyProject.board.hyeeun.service.HyeeunBoardService;
import test.toyProject.board.hyeeun.service.HyeeunCommentService;
import test.toyProject.board.seoyun.dto.SeoyunBoardDTO;
import test.toyProject.board.seoyun.dto.SeoyunCommentDTO;
import test.toyProject.user.dto.UserDTO;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

//    @GetMapping("/save")
//    public String saveForm() {
//        return "/board/hyeeun/save";
//    } // 그 이하의 주소를 각각의 메서드 중에서 맵핑 값이 일치하는 메서드 호출(get으로 받음)

    @GetMapping("/save")
    public String save(HttpSession session, RedirectAttributes redirectAttributes){
        // 세션에 로그인 정보 있는지 확인
        UserDTO loggedInUser = (UserDTO) session.getAttribute("loggedInUser");
        if(loggedInUser == null){
            // 로그인 되지 않은 경우 로그인 페이지로 리다이렉트
            redirectAttributes.addFlashAttribute("error", "로그인 필요");
            return "redirect:/user/login";
        }
        return "/board/hyeeun/save";
    }


    @PostMapping("/save")
    public String save(@ModelAttribute HyeeunBoardDTO boardDTO, HttpSession session) throws IOException {
        UserDTO loggedInUser = (UserDTO) session.getAttribute("loggedInUser");
        if(loggedInUser != null){
            // 로그인한 사용자의 정보를 게시물 작성자로 설정
            String boardWriter = loggedInUser.getFullName();

            boardDTO.setBoardCreatedTime(LocalDateTime.now());

            boardDTO.setBoardWriter(boardWriter);

            System.out.println("boardDTO = " + boardDTO);
            boardService.save(boardDTO);
            return "redirect:/board/hyeeun/paging";
        }else {
            return "redirect:/user/login";
        }
    }

//    @GetMapping("/")
//    public String findAll(Model model) {
//        // DB에서 전체 게시글 데이터를 가져와서 list.html에 보여준다.
//        List<HyeeunBoardDTO> boardDTOList = boardService.findAll();
//        model.addAttribute("boardList", boardDTOList); // 가져온 데이터를 model 객체에 담기
//        return "list"; // list.html로 리턴
//    } // 전체 목록을 db로부터 가져와야함 -> model 객체 사용, 목록 여러개를 가져온다 -> List<BoardDTO> boardDTOList = boardService.findAll();

    // 게시물 상세
    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model,
                           @PageableDefault(page = 1) Pageable pageable,
                           HttpSession session){
        HyeeunBoardDTO boardDTO = boardService.findById(id);
        UserDTO loggedInUser = (UserDTO) session.getAttribute("loggedInUser");

        // 댓글
        List<HyeeunCommentDTO> commentDTOList = commentService.findAll(id);
        model.addAttribute("commentList", commentDTOList);

        model.addAttribute("loggedInUser", loggedInUser.getFullName());
        model.addAttribute("board", boardDTO);
        model.addAttribute("page", pageable.getPageNumber());
        return "/board/hyeeun/detail";
    }

    // 수정
    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model,HttpSession session){
        UserDTO loggedInUser = (UserDTO) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            HyeeunBoardDTO boardDTO = boardService.findById(id);
            // 보드 작성자가 로그인한 사용자와 일치하는 경우에만 수정 폼을 제공
            if (boardDTO != null && boardDTO.getBoardWriter().equals(loggedInUser.getFullName())) {
                model.addAttribute("boardUpdate", boardDTO);
                model.addAttribute("loggedInUser", loggedInUser);
                return "/board/hyeeun/update";
            } else {
                // 보드 작성자와 로그인한 사용자가 다를 경우 처리
                // 예를 들어 에러 페이지로 리다이렉트하거나 에러 메시지를 보여줄 수 있음
                return "redirect:/error";
            }
        } else {
            return "redirect:/user/login";
        }
    }

    @PostMapping("/update")
    public String update(@ModelAttribute HyeeunBoardDTO boardDTO, Model model,HttpSession session) {
        UserDTO loggedInUser = (UserDTO) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            HyeeunBoardDTO originalBoard = boardService.findById(boardDTO.getId());
            // 보드 작성자가 로그인한 사용자와 일치하는 경우에만 업데이트
            if (originalBoard != null && originalBoard.getBoardWriter().equals(loggedInUser.getFullName())) {
                HyeeunBoardDTO board = boardService.update(boardDTO);
                model.addAttribute("board", board);
                return "redirect:/board/hyeeun/" + board.getId();
            } else {
                // 원래 게시물의 작성자와 로그인한 사용자가 다를 경우 처리
                // 예를 들어 에러 페이지로 리다이렉트하거나 에러 메시지를 보여줄 수 있음
                return "redirect:/error";
            }
        } else {
            return "redirect:/user/login";
        }
    }

    // 삭제
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session) {
        UserDTO loggedInUser = (UserDTO) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            HyeeunBoardDTO boardDTO = boardService.findById(id);
            if (boardDTO != null && boardDTO.getBoardWriter().equals(loggedInUser.getFullName())) {
                boardService.delete(id);
                return "redirect:/board/hyeeun/paging";
            } else {
                // 에러 처리 또는 다른 페이지로 리다이렉트
                return "redirect:/error";
            }
        } else {
            return "redirect:/user/login";
        }
    }

    @GetMapping("/listCount")
    @ResponseBody // JSON 형식으로 응답하기 위해 추가
    public ResponseEntity<Map<String, Integer>> getTotalPostCount() {
        int totalCount = boardService.getTotalPostCount();

        Map<String, Integer> response = new HashMap<>();
        response.put("totalCount", totalCount);

        return ResponseEntity.ok().body(response);
    }
    // /board/paging?page=1
    @GetMapping("/paging")
    public String findAll(@PageableDefault(page = 1, size = 10) Pageable pageable, Model model) {
        Page<HyeeunBoardDTO> boardList = boardService.paging(pageable);
        int blockLimit = 3;
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10 ~~
        int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : boardList.getTotalPages();

        // page 갯수 20개
        // 현재 사용자가 3페이지
        // 1 2 3
        // 현재 사용자가 7페이지
        // 7 8 9
        // 보여지는 페이지 갯수 3개
        // 총 페이지 갯수 8개

        model.addAttribute("boardList", boardList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "/board/hyeeun/paging";
    }

    }






