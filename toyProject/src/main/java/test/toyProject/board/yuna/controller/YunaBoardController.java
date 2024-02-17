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
import test.toyProject.board.yuna.dto.YunaBoardDTO;
import test.toyProject.board.yuna.service.YunaBoardService;
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
    @GetMapping("/yunaBoard")
    public String yunaBoard() {
        return "/board/yuna/yunaBoard";
    }


    @GetMapping("/save")
    public String saveForm(HttpSession session, Model model){

        // 세션에서 로그인한 사용자의 이메일 가져오기
//        String loginEmail = (String) session.getAttribute("loginEmail");
// 세션에 로그인 정보 있는지 확인
        // 세션에 로그인 정보가 없으면 로그인 페이지로 리다이렉트
//        if (loginEmail == null) {
//            return "redirect:/user/login";
//        }

        // ***서윤 씀*** user 폴더 만든거로 합치느라 변경했습니다!
        UserDTO loggedInUser = (UserDTO) session.getAttribute("loggedInUser");
        if(loggedInUser == null) {
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
            return "/board/yuna/yunaBoard";
        } else {
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
                           @PageableDefault(page=1) Pageable pageable) {

        boardService.updateHits(id);
        YunaBoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);
        model.addAttribute("page", pageable.getPageNumber());
        return "board/yuna/detail";
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model, HttpSession session) {
        UserDTO loggedInUser = (UserDTO) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            YunaBoardDTO boardDTO = boardService.findById(id);
            if (boardDTO != null && boardDTO.getBoardWriter().equals(loggedInUser.getFullName())) {
                model.addAttribute("loggedInUser", loggedInUser);
                model.addAttribute("boardUpdate", boardDTO);
                return "board/yuna/update";
            } else {
                return "redirect:/error";
            }
        } else {
            return "redirect:/user/login";
        }
    }

    @PostMapping("/update")
    public String update(@ModelAttribute YunaBoardDTO boardDTO, Model model, HttpSession session) {
        UserDTO loggedInUser = (UserDTO) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            YunaBoardDTO originalBoard = boardService.findById(boardDTO.getId());
            if (originalBoard != null && originalBoard.getBoardWriter().equals(loggedInUser.getFullName())) {
                YunaBoardDTO board = boardService.update(boardDTO);
                model.addAttribute("board", board);
                // 수정된 게시물 URL
                return "redirect:/board/yuna/" + board.getId();
            } else {
                return "redirect:/error";
            }
        } else {
            return "redirect:/user/login";
        }

        // return "yuna/detail";
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
                return "redirect:/error";
            }
        } else {
            return "redirect:/user/login";
        }
    }

    @GetMapping("/paging")
    public String paging(@PageableDefault(page = 1) Pageable pageable, Model model) {
//        pageable.getPageNumber();
        Page<YunaBoardDTO> boardList = boardService.paging(pageable);
        int blockLimit = 3; // 페이지 번호의 개수
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10 ~~
        int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : boardList.getTotalPages();


        model.addAttribute("boardList", boardList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "/board/yuna/paging";

    }
}
