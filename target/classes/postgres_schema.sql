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