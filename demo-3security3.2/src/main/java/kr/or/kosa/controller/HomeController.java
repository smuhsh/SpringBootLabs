package kr.or.kosa.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import kr.or.kosa.dto.Users;
import kr.or.kosa.service.UserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HomeController {

	
	@Autowired
	private UserService userService; // 회원가입 ....
	
    @GetMapping({"","/"})
    public String home(Model model, Principal principal){
        String loginId = principal !=null ? principal.getName() : "guest";
        model.addAttribute("loginId",loginId);
        return "index";
    }
    
    /***
     * 
     * @param auth
     * @param model
     * @return
     * CustomerAccessDeniedHandler  response.sendRedirect("/exception");
     */
    @GetMapping("/exception")
    public String exception(Authentication auth , Model model){
        log.info("인증 예외 처리");
        log.info("auth : " + auth.toString());
        model.addAttribute("msg","인증거부 : " + auth.toString());
        return "/exception";
    }
    
    @GetMapping("/login")
    public String login(){
        log.info("로그인 처리");
       
        return "/login";
    }
    
    @GetMapping("/join") //화면보여주 
    public String join(){
        log.info("회원 가입 처리");
        return "/join";
    }


    @PostMapping("/join") //처리해주 
    public String joinPro(Users user) throws  Exception {
        int result =userService.join(user);

        if(result > 0 ){
            return "redirect:/login";
        }

        return "redirect:/join?error";

    }

}
