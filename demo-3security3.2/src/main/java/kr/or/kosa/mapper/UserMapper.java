package kr.or.kosa.mapper;

import org.apache.ibatis.annotations.Mapper;

import kr.or.kosa.dto.UserAuth;
import kr.or.kosa.dto.Users;

@Mapper
public interface UserMapper {
	
	//로그인 사용자 인증 
	public Users login(String username);
	
	
	//회원가입
    public int join(Users user) throws Exception;

    //회원 권한 등록
    public int insertAuth(UserAuth userAuth) throws Exception;
}
