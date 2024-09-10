package kr.or.kosa.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.or.kosa.model.User;

@Mapper
public interface UserMapper {
	//추상자원을 갖는다, 추상함수
	//CRUD 함수 ... mapper.xml 연결시켜서 동기화 할거다 ...
	
	List<User> selectAll(); //목록조회
	User selectById(long id); //상세조회
	void insert(User user);
	void update(User user);
	void delete(long id);
	
	//필요하다면 LIKE 검색... Order by ... 함수 추가...
	
}
