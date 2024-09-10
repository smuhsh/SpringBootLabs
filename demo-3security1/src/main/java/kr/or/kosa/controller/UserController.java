package kr.or.kosa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {
    /**
     *
     * @return
     */
    @GetMapping({"/",""})
    public String index(){
        log.info("[[[  /user   ]]]");
        return "/user/index";
    }
}