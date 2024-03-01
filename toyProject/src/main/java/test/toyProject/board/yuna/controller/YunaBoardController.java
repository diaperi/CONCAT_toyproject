package test.toyProject.board.yuna.controller;

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
import test.toyProject.board.seoyun.dto.SeoyunBoardDTO;
import test.toyProject.board.seoyun.dto.SeoyunCommentDTO;
import test.toyProject.board.yuna.dto.YunaBoardDTO;
import test.toyProject.board.yuna.dto.YunaCommentDTO;
import test.toyProject.board.yuna.service.YunaBoardService;
import test.toyProject.board.yuna.service.YunaCommentService;
import test.toyProject.user.dto.UserDTO;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board/yuna")
public class YunaBoardController {
    private final YunaBoardService boardService;
    private final YunaCommentService yunaCommentService;
    @GetMapping("/yunaBoard")
    public String yunaBoard() {
        return "/board/yuna/yunaBoard";
    }


    @GetMapping("/save")
    public String save(HttpSession session, RedirectAttributes redirectAttributes){
        // 세션에 로그인 정보 있는지 확인
        UserDTO loggedInUser = (UserDTO) session.getAttribute("loggedInUser");
        if(loggedInUser == null){
            // 로그인 되지 않은 경우 로그인 페이지로 리다이렉트
            redirectAttributes.addFlashAttribute("error", "로그인 필요");
            return "redirect:/user/login";
        }
        return "/board/yuna/save";
    }


    @PostMapping("/save")
    public String save(@ModelAttribute YunaBoardDTO boardDTO, HttpSession session) throws IOException {
        UserDTO loggedInUser = (UserDTO) session.getAttribute("loggedInUser");
        if(loggedInUser != null){
            // 로그인한 사용자의 정보를 게시물 작성자로 설정
            String boardWriter = loggedInUser.getFullName();

            boardDTO.setBoardCreatedTime(LocalDateTime.now());

            boardDTO.setBoardWriter(boardWriter);

            System.out.println("boardDTO = " + boardDTO);
            boardService.save(boardDTO);
            return "redirect:/board/yuna/paging";
        }else {
            return "redirect:/user/login";
        }
    }


    @GetMapping("/")
    public String findAll(Model model) {
        List<YunaBoardDTO> boardDTOList = boardService.findAll();
        model.addAttribute("boardList", boardDTOList);
        return "yuna/list";
    }

    // 추가
    // 게시글 목록에서 전체 게시글 카운트
    @GetMapping("/listCount")
    @ResponseBody // JSON 형식으로 응답하기 위해 추가
    public ResponseEntity<Map<String, Integer>> getTotalPostCount() {
        int totalCount = boardService.getTotalPostCount();

        Map<String, Integer> response = new HashMap<>();
        response.put("totalCount", totalCount);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model,
                           @PageableDefault(page = 1) Pageable pageable,
                           HttpSession session){
        YunaBoardDTO boardDTO = boardService.findById(id);
        UserDTO loggedInUser = (UserDTO) session.getAttribute("loggedInUser");

        // 댓글
        List<YunaCommentDTO> commentDTOList = yunaCommentService.findAll(id);
        model.addAttribute("commentList", commentDTOList);

        model.addAttribute("loggedInUser", loggedInUser.getFullName());
        model.addAttribute("board", boardDTO);
        model.addAttribute("page", pageable.getPageNumber());
        return "/board/seoyun/boardDetail";
    }

    // 수정
    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model,HttpSession session){
        UserDTO loggedInUser = (UserDTO) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            YunaBoardDTO boardDTO = boardService.findById(id);
            // 보드 작성자가 로그인한 사용자와 일치하는 경우에만 수정 폼을 제공
            if (boardDTO != null && boardDTO.getBoardWriter().equals(loggedInUser.getFullName())) {
                model.addAttribute("boardUpdate", boardDTO);
                model.addAttribute("loggedInUser", loggedInUser);
                return "/board/yuna/update";
            } else {
                // 보드 작성자와 로그인한 사용자가 다를 경우 JavaScript를 사용하여 알림창을 띄움
                model.addAttribute("loggedInUser", loggedInUser.getFullName());
                return "/board/yuna/detail";
            }
        } else {
            return "redirect:/user/login";
        }
    }

    @PostMapping("/update")
    public String update(@ModelAttribute YunaBoardDTO boardDTO, Model model,HttpSession session) {
        UserDTO loggedInUser = (UserDTO) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            YunaBoardDTO originalBoard = boardService.findById(boardDTO.getId());
            // 보드 작성자가 로그인한 사용자와 일치하는 경우에만 업데이트
            if (originalBoard != null && originalBoard.getBoardWriter().equals(loggedInUser.getFullName())) {
                YunaBoardDTO board = boardService.update(boardDTO);
                model.addAttribute("board", board);
                return "redirect:/board/seoyun/" + board.getId();
            } else {
                // 원래 게시물의 작성자와 로그인한 사용자가 다를 경우 처리
                // 예를 들어 에러 페이지로 리다이렉트하거나 에러 메시지를 보여줄 수 있음
                return "redirect:/error";
            }
        } else {
            return "redirect:/user/login";
        }
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session) {
        UserDTO loggedInUser = (UserDTO) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            YunaBoardDTO boardDTO = boardService.findById(id);
            if (boardDTO != null && boardDTO.getBoardWriter().equals(loggedInUser.getFullName())) {
                boardService.delete(id);
                return "redirect:/board/yuna/paging";
            } else {
                // 에러 처리 또는 다른 페이지로 리다이렉트
                return "redirect:/error";
            }
        } else {
            return "redirect:/user/login";
        }
    }

    @GetMapping("/paging")
    public String findAll(@PageableDefault(page = 1, size = 10) Pageable pageable, Model model) {
        Page<YunaBoardDTO> boardList = boardService.paging(pageable);
        int blockLimit = 3;
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10 ~~
        int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : boardList.getTotalPages();

        model.addAttribute("boardList", boardList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "/board/yuna/paging";
    }
}
