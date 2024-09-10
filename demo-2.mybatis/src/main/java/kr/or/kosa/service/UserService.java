package kr.or.kosa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.kosa.mapper.UserMapper;
import kr.or.kosa.model.User;

@Service
public class UserService {
	
	@Autowired
	private UserMapper userMapper;
	
	public List<User> getAllUsers(){
		return userMapper.selectAll(); //xml 파일 실행 ... 쿼리 ... 결과 ... 객체 리턴
	}
	public User getUserById(long id) {
		return userMapper.selectById(id);
	}
	
	public void createUser(User user) {
		userMapper.insert(user);
	}
	
	public void updateUser(User user) {
		userMapper.update(user);
	}
	
	public void deleteUser(long id) {
		userMapper.delete(id);
	}
}
