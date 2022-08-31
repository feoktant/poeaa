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
