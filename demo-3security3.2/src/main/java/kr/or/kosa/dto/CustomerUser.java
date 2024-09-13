package kr.or.kosa.dto;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomerUser implements UserDetails{

	//private static final long serialVersionUID = 1L;
	
	
	//사용자 만든  DTO
	private Users user;
	
	public CustomerUser(Users user) {
		this.user = user;
	}
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		
		return user.getAuthList()
			   .stream()
			   .map((auth) -> new SimpleGrantedAuthority(auth.getAuth())).collect(Collectors.toList());
			   //권한 목록을 가지고 있어요
		       // [0 방에 .UserAuth][1 방에 .UserAuth][UserAuth][UserAuth]
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getUserPw();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getUserId();
	}

	

}
