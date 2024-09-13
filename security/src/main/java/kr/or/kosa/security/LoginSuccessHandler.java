package kr.or.kosa.security;


import kr.or.kosa.dto.CustomerUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//로그인 성공 처리 클래스
@Slf4j
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                        HttpServletResponse response, 
                                        Authentication authentication) throws IOException, ServletException {
        log.info("로그인 인증 성공");

       // User user = (User)authentication.getPrincipal();
        CustomerUser user = (CustomerUser)authentication.getPrincipal();

        log.info("아이디 :" + user.getUsername());
        log.info("패스워드 :" + user.getPassword());
        log.info("권한 :" + user.getAuthorities());

        response.sendRedirect("/admin");
    }
}
