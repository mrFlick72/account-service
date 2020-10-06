CREATE TABLE ACCOUNT(
  MAIL VARCHAR(255) PRIMARY KEY           ,
  FIRST_NAME VARCHAR(255) NOT NULL                      ,
  LAST_NAME VARCHAR(255) NOT NULL           ,
  BIRTH_DATE DATE  NOT NULL                      ,
  PHONE VARCHAR(255)                ,
  locale varchar(5) NOT NULL
);