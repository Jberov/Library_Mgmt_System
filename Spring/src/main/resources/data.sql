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
  (4,'Тютюн', 'Димитър Димов', 'История за човешки характери, поквара и любов на фона на ВСВ.'),
  (6,'Клетниците', 'Виктор Юго', 'Разтърсваща история за човешкия падеж и неговото възстановяване.');