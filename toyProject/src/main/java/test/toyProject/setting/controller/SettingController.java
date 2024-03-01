package test.toyProject.setting.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
//@RequestMapping("/setting")
public class SettingController {

    // 설정 홈 이동
    @GetMapping("/setting/settingHome")
    public String settingHome(){
        return "/setting/settingHome";
    }

    // 개인정보 이동
    @GetMapping("/setting/personal")
    public String personal(){
        return "/setting/personal";
    }

    // 단어 저장함 이동
    @GetMapping("/setting/vocabularyBook")
    public String vocabularyBook(){
        return "/setting/vocabularyBook";
    }


    @GetMapping("/main/communityMain")
    public String communityMain(){
        return "/main/communityMain";
    }
}
