package kr.or.kosa.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//정상 동작
//조건 : User member (field 는 테이블에서는 컬럼명과 동일하다 (자동화))

/*
 id NUMBER PRIMARY KEY,
 username VARCHAR2(50),
 password VARCHAR2(50),
 email VARCHAR2(50)
 * */

/*
 * 1. DTO 생성하기
 * 2. Map(key,value)
 * */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder //편해요
public class User {
	
	private long id;
	private String username;
	private String password;
	private String email;

}
