create table users
(
    id       integer                not null auto_increment,
    email    varchar(320)           not null unique,
    phone    varchar(12)            not null unique,
    password varchar(200)           not null,
    role     enum ('USER', 'ADMIN') not null,
    primary key (id)
);

create table user_info
(
    user_id    integer     not null unique,
    name       varchar(30) not null,
    surname    varchar(30) not null,
    patronymic varchar(30) not null,
    primary key (user_id),
    constraint user_info_id_fk foreign key
        (user_id) references users (id)
);

create table customer
(
    id         integer                                    not null auto_increment,
    user_id    integer                                    not null unique,
    type       enum ('PERSON', 'ENTREPRENEUR', 'COMPANY') not null,
    region     varchar(100)                               not null,
    district   varchar(100)                               not null,
    settlement varchar(100)                               not null,
    primary key (id),
    constraint customer_user_id_fk
        foreign key (user_id) references users (id)
);

create table person_info
(
    customer_id integer not null unique,
    sex         bit     not null,
    birth_date  date    not null,
    primary key (customer_id),
    constraint pers_info_cust_id_fk
        foreign key (customer_id) references customer (id)
);

create table company_info
(
    customer_id        integer      not null unique,
    name               varchar(100) not null unique,
    egrpou             integer      not null unique,
    incorporation_date date         not null,
    primary key (customer_id),
    constraint comp_info_cust_id_fk
        foreign key (customer_id) references customer (id)
);