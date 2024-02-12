package test.toyProject.user.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import test.toyProject.user.dto.UserDTO;
import test.toyProject.user.entity.UserEntity;
import test.toyProject.user.repository.UserRepository;
import test.toyProject.user.service.UserService;
@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/signup")
    public String showSignupForm(){
        return "/user/signup";
    }

    @PostMapping("/save")
    public String signup(@ModelAttribute UserDTO userDTO) {
       System.out.println("UserController.save");
       System.out.println("userDTO = " + userDTO);
       userService.save(userDTO);
       return "/user/login";
    }

    @GetMapping("/login")
    public String loginForm(){
        return "/user/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute UserDTO userDTO, HttpSession session){
        UserDTO loginResult = userService.login(userDTO);
        if(loginResult != null){
            // login 성공
            session.setAttribute("loggedInUser", loginResult);
            return "/index";
        }else{
            // login 실패
            return "/user/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "/index";
    }

    @PostMapping("/email-check")
    public @ResponseBody String emailCheck(@RequestParam("email") String email){
        System.out.println("memberEmail = " + email);
        String checkResult = userService.emailCheck(email);
        return checkResult;
    }
    @GetMapping("/check-session")
    public String checkSession(HttpSession session){
        UserDTO loggedInUser = (UserDTO) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            System.out.println("세션에 저장된 사용자 정보: " + loggedInUser);
        } else {
            System.out.println("세션에 사용자 정보가 설정되어 있지 않음");
        }
        return "redirect:/";
    }




}
