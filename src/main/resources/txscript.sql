CREATE TABLE products
(
    ID   int primary key,
    name varchar,
    type varchar
);

CREATE TABLE contracts
(
    ID         int primary key,
    product    int,
    revenue    decimal,
    dateSigned date
);

CREATE TABLE revenueRecognitions
(
    contract     int,
    amount       decimal,
    recognizedOn date,
    PRIMARY KEY (contract, recognizedOn)
);

---------------------------------------------

INSERT INTO products
VALUES (1, 'Vi word processor', 'W'),
       (2, 'Postgres Database', 'D'),
       (3, 'Excel Spreadsheet', 'S');

INSERT INTO contracts
VALUES (10, 1, 20, '2022-01-01');
