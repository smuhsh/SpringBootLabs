package kr.or.kosa.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import kr.or.kosa.dto.CustomerUser;
import kr.or.kosa.dto.Users;
import kr.or.kosa.mapper.UserMapper;

//인증에 대한 처리를 개발자가 원하는대로 ... UserDetailsService 재정의 여러분 마음 : mybatis, jpa, 원하는 방법 제공
//loadUserByUsername 재정의
@Service
public class CustomerDetailService implements UserDetailsService{
	
	@Autowired
	private UserMapper userMapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//개발자가 원하는 것은 spring 자동 처리되는 것이 아니고 내가 정의한 mybatis 통해서 인증과 권한 정보를 담겠다
		
		Users user = userMapper.login(username);
		if(user == null) {
			throw new UsernameNotFoundException("요청하신 ID 가 없습니다."+username);
		}
		
		CustomerUser customerUser = new CustomerUser(user);
		//getAuthorities(), getUsername() 함수 활용해서 정보 얻는다
		return customerUser;
	}

}
