DROP TABLE IF EXISTS BOOKS;
CREATE TABLE BOOKS(
isbn LONG  AUTO_INCREMENT PRIMARY KEY,
count_books INT NOT NULL,
author VARCHAR(30) NOT NULL,
name VARCHAR(30) NOT NULL UNIQUE,
description VARCHAR(250)
);

INSERT INTO BOOKS (count_books, author, name, description) VALUES
  (2,'Иван Вазов', 'Под Игото', 'В малко градче пристига странник и им показва значението на свободата'),
  (4,'Димитър Димов','Тютюн' , 'История за човешки характери, поквара и любов на фона на ВСВ.'),
  (6,'Клетниците', 'Виктор Юго', 'Разтърсваща история за човешкия падеж и неговото възстановяване.');

DROP TABLE IF EXISTS USERS;
CREATE TABLE USERS(
id LONG  AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(30) NOT NULL UNIQUE
);

INSERT INTO USERS (name) VALUES
  ('JBaller'),
  ('Hero'),
  ('User');

DROP TABLE IF EXISTS ACTIVITY;
CREATE TABLE ACTIVITY(
    id LONG AUTO_INCREMENT PRIMARY KEY,
    books_isbn LONG NOT NULL,
    status ENUM('TAKEN','RETURNED') NOT NULL,
    user_id LONG NOT NULL,
    FOREIGN KEY (books_isbn) REFERENCES BOOKS(isbn),
    FOREIGN KEY (user_id) REFERENCES USERS(id)
);