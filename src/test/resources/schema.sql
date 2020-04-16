CREATE TABLE ACCOUNT(
  MAIL VARCHAR(255) PRIMARY KEY           ,
  FIRST_NAME VARCHAR(255) NOT NULL                      ,
  LAST_NAME VARCHAR(255) NOT NULL           ,
  BIRTH_DATE DATE  NOT NULL                      ,
  PHONE VARCHAR(255)                ,
  locale varchar(5) NOT NULL
);


INSERT INTO ACCOUNT (first_Name,last_Name, birth_Date, mail, locale) VALUES ('Valerio', 'Vaudi', '1970-01-01', 'valerio.vaudi@test.com', 'en');
INSERT INTO ACCOUNT (first_Name,last_Name, birth_Date, mail, phone, locale) VALUES ('Valerio', 'Vaudi', '1970-01-01', 'valerio.vaudi-with-phone@test.com','+39 333 2255112' ,'en');