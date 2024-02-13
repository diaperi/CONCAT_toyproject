package test.toyProject.board.hyeeun.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HyeeunHomeController {
    @GetMapping("/")
    public String index(){
        return"index";
    }
}
