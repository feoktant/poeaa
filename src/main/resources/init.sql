-- Chapter 9 -----------------------------------------------------------------
-- Example: Revenue Recognition
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

------
INSERT INTO products
VALUES (1, 'Vi word processor', 'W'),
       (2, 'Postgres Database', 'D'),
       (3, 'Excel Spreadsheet', 'S');
INSERT INTO contracts
VALUES (10, 1, 20, '2022-01-01');

-- Chapter 10 ----------------------------------------------------------------
-- Example: A Person Record
create table people
(
    ID                   int primary key,
    lastname             varchar,
    firstname            varchar,
    number_of_dependents int
);

-- Chapter 12 ----------------------------------------------------------------
-- Example: Using a Key Table
CREATE TABLE keys
(
    name   varchar primary key,
    nextID int
);

INSERT INTO keys
VALUES ('orders', 1);

-- Example: Using a Compound Key
CREATE TABLE orders
(
    ID       int primary key,
    customer varchar
);
CREATE TABLE line_items
(
    orderID int,
    seq     int,
    amount  int,
    product varchar,
    primary key (orderID, seq)
);

-- Association Table Mapping
-- Example: Using Direct SQL
create table employees
(
    ID        int primary key,
    firstname varchar,
    lastname  varchar
);
create table skills
(
    ID   int primary key,
    name varchar
);
create table employeeSkills
(
    employeeID int,
    skillID    int,
    primary key (employeeID, skillID)
);

------
insert into employees
values (1, 'Anton', 'Feoktistov');
insert into skills
values (1, 'Java'),
       (2, 'Scala'),
       (3, 'SQL');
insert into employeeSkills
values (1, 1),
       (1, 2),
       (1, 3);

-- Serialized LOB
create table customers
(
    ID          int primary key,
    name        varchar,
    departments varchar
);

-- Chapter 16 ----------------------------------------------------------------
-- TBD, clashes with customers in ch12
