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


