package kr.or.kosa.mapper;

import org.apache.ibatis.annotations.Mapper;

import kr.or.kosa.dto.Users;
@Mapper
public interface UserMapper {
	public Users login(String username);

}
