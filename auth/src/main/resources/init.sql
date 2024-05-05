CREATE TABLE users
(
    id          integer      not null auto_increment,
    companyName        varchar(30),
    surname     varchar(30),
    patronymic  varchar(30),
    role        enum ('ADMIN', 'MANAGER', 'USER'),
    email       varchar(320) not null unique,
    phone       varchar(12)  not null unique,
    password    varchar(200) not null,
    created_at  datetime     not null,
    modified_at datetime     not null,
    primary key (id)
);