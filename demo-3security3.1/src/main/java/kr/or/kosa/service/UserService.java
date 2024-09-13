package kr.or.kosa.service;

import kr.or.kosa.dto.UserAuth;
import kr.or.kosa.dto.Users;

public interface UserService {

       //로그인 사용자 인증
    public Users login(String username);

    //회원가입
    public int join(Users user) throws Exception;

    //회원 권한 등록
    public int insertAuth(UserAuth userAuth) throws Exception;
}
