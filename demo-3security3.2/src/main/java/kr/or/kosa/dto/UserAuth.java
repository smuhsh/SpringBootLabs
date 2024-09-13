package kr.or.kosa.dto;

import lombok.Data;

/*
CREATE TABLE `user_auth` (
      auth_no int NOT NULL AUTO_INCREMENT       -- 권한번호
    , user_id varchar(100) NOT NULL             -- 아이디
    , auth varchar(100) NOT NULL                -- 권한 (ROLE_USER, ROLE_ADMIN, ...)
    , PRIMARY KEY(auth_no)
);

*/
@Data
public class UserAuth {
	private int authNo;
	private String userId;
	private String auth;  //getter 
}
