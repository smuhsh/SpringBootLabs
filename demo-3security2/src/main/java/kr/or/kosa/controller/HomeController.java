package kr.or.kosa.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HomeController {

    @GetMapping({"","/"})
    public String home(Model model, Principal principal){
        String loginId = principal !=null ? principal.getName() : "guest";
        model.addAttribute("loginId",loginId);
        return "index";
    }
}
