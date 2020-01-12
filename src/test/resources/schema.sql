CREATE TABLE ACCOUNT(
  MAIL VARCHAR(255) PRIMARY KEY           ,
  PASSWORD VARCHAR(255) NOT NULL                      ,
  USER_ROLES VARCHAR(255) NOT NULL                      ,
  FIRST_NAME VARCHAR(255) NOT NULL                      ,
  LAST_NAME VARCHAR(255) NOT NULL           ,
  BIRTH_DATE DATE  NOT NULL                      ,
  PHONE VARCHAR(255)                ,
    ENABLE BOOL NOT NULL,
	locale varchar(5) NOT NULL
);


INSERT INTO ACCOUNT (first_Name,last_Name, birth_Date, mail, password, user_roles, enable, locale) VALUES ('Valerio', 'Vaudi', '1970-01-01', 'valerio.vaudi@test.com', 'secret', 'ROLE_USER,ROLE_ADMIN', true ,'en');
INSERT INTO ACCOUNT (first_Name,last_Name, birth_Date, mail, password, user_roles, phone, enable, locale) VALUES ('Valerio', 'Vaudi', '1970-01-01', 'valerio.vaudi-with-phone@test.com', 'secret', 'ROLE_USER,ROLE_ADMIN', '+39 333 2255112',true ,'en');