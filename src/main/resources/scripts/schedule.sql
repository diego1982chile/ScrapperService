drop table if exists SCHEDULE;

CREATE TABLE SCHEDULE(
   ID INTEGER PRIMARY KEY AUTOINCREMENT,
   SCHEDULE TEXT NOT NULL,
   RETAILER_ID INTEGER NOT NULL,
   FOREIGN KEY (RETAILER_ID)
      REFERENCES RETAILER (ID)
);