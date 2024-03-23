create table users
(
    id       integer not null auto_increment,
    login    varchar(20)  not null,
    password varchar(200) not null,
    name     varchar(30),
    surname  varchar(30),
    role     enum ('ADMIN','USER') not null,
    primary key (id)
) engine=InnoDB;

alter table users
    add constraint users_login_uk unique (login);
