drop table if exists CLIENT;

CREATE TABLE CLIENT(
   ID INTEGER PRIMARY KEY AUTOINCREMENT,
   NAME TEXT NOT NULL UNIQUE
);