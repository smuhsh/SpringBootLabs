package kr.or.kosa.security;


import kr.or.kosa.dto.CustomerUser;
import kr.or.kosa.dto.Users;
import kr.or.kosa.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerDetailService  implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Mybatis 사용해서  사용자 정보 조회

        Users user = userMapper.login(username); // DB정보 조회
        if(user == null){
            throw  new UsernameNotFoundException("사용자를 찿을 수 없습니다" +  username);
        }

        CustomerUser customerUser = new CustomerUser(user);  //public class CustomerUser  implements UserDetails  에 정보 제공 (사용자)

        return customerUser;
    }
}
