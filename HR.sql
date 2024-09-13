SELECT COALESCE(NULL,'2') FROM DUAL;
SELECT NULLIF('A','A') FROM DUAL;
SELECT NVL(NULL,0) + 10 FROM DUAL;
SELECT NVL(NULL,'A') FROM DUAL;


SELECT LOWER(A.agency_name)
FROM AGENCY A;

SELECT UPPER(sqldeveloper) FROM DUAL;

SELECT SYSDATE FROM DUAL;

select department_id as departmentId,  	  department_name as departmentName   from departments;
select 		d.manager_id as managerId, e.first_name as firstName   from departments d join employees e 		on d.manager_id = e.employee_id   order by d.manager_id;

CREATE TABLE USERS (
 id NUMBER PRIMARY KEY,
 username VARCHAR2(50),
 password VARCHAR2(50),
 email VARCHAR2(50)
);

CREATE SEQUENCE USER_ID;

COMMIT;

SELECT * FROM USERS;

INSERT INTO USERS (id, username, password, email) VALUES(USER_ID.NEXTVAL,'admin','1004','admin@naver.com'); 
UPDATE USERS SET username = "shhwang", email="shhwang@naver.com" WHERE id= 1;

SELECT * FROM USERS WHERE ID=1;


--2024-09-11 09:45
-- 시퀀스 생성
CREATE SEQUENCE USER_SEQ
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

-- user 테이블 생성
CREATE TABLE "USER" (
  USER_NO NUMBER(10) NOT NULL,
  USER_ID VARCHAR2(100) NOT NULL,
  USER_PW VARCHAR2(200) NOT NULL,
  NAME VARCHAR2(100) NOT NULL,
  EMAIL VARCHAR2(200),
  REG_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  UPD_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  ENABLED NUMBER(1) DEFAULT 1,
  CONSTRAINT PK_USER PRIMARY KEY (USER_NO)
);
-- 시퀀스 생성
CREATE SEQUENCE USER_AUTH_SEQ
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

-- user_auth 테이블 생성
CREATE TABLE USER_AUTH (
  AUTH_NO NUMBER(10) NOT NULL,
  USER_ID VARCHAR2(100) NOT NULL,
  AUTH VARCHAR2(100) NOT NULL,
  CONSTRAINT PK_USER_AUTH PRIMARY KEY (AUTH_NO)
);
insert into "USER"(USER_NO,user_id,user_pw,name,email)
values(USER_SEQ.nextval ,'user','$2a$12$XaNpE2vVBM7x4ZCqlafgxu2W5vw9SXnvMgUZQnzOkFSZt605JHuqq','사용자','user@naver.com');

insert into "USER"(USER_NO,user_id,user_pw,name,email)
values(USER_SEQ.nextval,'admin','$2a$12$XaNpE2vVBM7x4ZCqlafgxu2W5vw9SXnvMgUZQnzOkFSZt605JHuqq','관리자','admin@naver.com');


-- 권한
insert into user_auth(AUTH_NO,user_id , auth)
values(USER_AUTH_SEQ.nextval, 'user','ROLE_USER');

-- 관리자
insert into user_auth(AUTH_NO,user_id , auth)
values(USER_AUTH_SEQ.nextval, 'admin','ROLE_USER');
insert into user_auth(AUTH_NO,user_id , auth)
values(USER_AUTH_SEQ.nextval, 'admin','ROLE_ADMIN');

COMMIT;

SELECT * FROM "USER";

select user_id as username , user_pw as password , enabled from "USER" where user_id='user';
SELECT u.user_no
    ,u.user_id
    ,user_pw
    ,name
    ,email
    ,enabled
    ,reg_date
    ,upd_date
    ,auth
FROM "USER" u  JOIN user_auth auth ON u.user_id = auth.user_id
WHERE u.user_id = 'user';

