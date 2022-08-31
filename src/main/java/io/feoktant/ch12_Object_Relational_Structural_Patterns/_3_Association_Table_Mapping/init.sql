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
