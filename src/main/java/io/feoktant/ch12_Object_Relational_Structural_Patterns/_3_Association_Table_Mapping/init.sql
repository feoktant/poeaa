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
