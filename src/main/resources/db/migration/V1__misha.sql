create table product
(
    id   integer not null auto_increment,
    price float(53)    not null,
    name  varchar(300) not null unique,
    constraint product_pk primary key (id)
) engine=InnoDB;
