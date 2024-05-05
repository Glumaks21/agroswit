CREATE TABLE customer
(
    user_id    integer     not null unique,
    type       enum ( 'PERSON', 'COMPANY', 'ENTREPRENEUR') not null ,
    district   enum ('VINNYTSIA', 'VOLYN', 'DNIPROPETROVSK', 'DONETSK', 'ZHYTOMYR', 'ZAKARPATTIA',
        'ZAPORIZHIA', 'IVANO_FRANKIVSK', 'KYIV', 'KYIV_CITY', 'KIROVOHRAD', 'LUGANSK',
        'LVIV', 'MYKOLAIV', 'ODESA', 'POLTAVA', 'RIVNE', 'SUMY', 'TERNOPIL', 'KHARKIV',
        'KHERSON', 'KHMELNYTSKY', 'CHERKASY', 'CHERNIHIV', 'CHERNIVTSI') not null ,
    region     varchar(50) not null,
    settlement varchar(50) not null,
    primary key (user_id),
    constraint customer_user_id_fk
        foreign key (user_id) references users (id)
);

CREATE TABLE person
(
    customer_id integer                 not null,
    id_number   varchar(10)             not null unique,
    sex         enum ('MALE', 'FEMALE') not null,
    birth_date  date                    not null,
    primary key (customer_id),
    constraint person_cust_id_fk
        foreign key (customer_id) references customer (user_id)
);

CREATE TABLE entrepreneur
(
    customer_id integer     not null,
    company_name        varchar(50) not null unique,
    id_number   varchar(10) not null unique,
    primary key (customer_id),
    constraint entrepreneur_cust_id_fk
        foreign key (customer_id) references customer (user_id)
);

CREATE TABLE company
(
    customer_id        integer     not null,
    company_name               varchar(50) not null,
    egrpou             varchar(12) not null unique,
    incorporation_date date        not null,
    primary key (customer_id),
    constraint company_cust_id_fk
        foreign key (customer_id) references customer (user_id)
);

CREATE TABLE manager
(
    user_id  integer not null,
    district enum ('VINNYTSIA', 'VOLYN', 'DNIPROPETROVSK', 'DONETSK', 'ZHYTOMYR', 'ZAKARPATTIA',
        'ZAPORIZHIA', 'IVANO_FRANKIVSK', 'KYIV', 'KYIV_CITY', 'KIROVOHRAD', 'LUGANSK',
        'LVIV', 'MYKOLAIV', 'ODESA', 'POLTAVA', 'RIVNE', 'SUMY', 'TERNOPIL', 'KHARKIV',
        'KHERSON', 'KHMELNYTSKY', 'CHERKASY', 'CHERNIHIV', 'CHERNIVTSI'),
    primary key (user_id),
    constraint manager_user_id_fk
        foreign key (user_id) references users (id)
);

CREATE TABLE manager_orders
(
    manager_id integer not null,
    order_id   integer not null,
    primary key (manager_id, order_id),
    constraint mngr_orders_m_id_fk
        foreign key (manager_id)
            references users (id),
    constraint mngr_orders_o_id_fk
        foreign key (order_id)
            references orders (id)
);

ALTER TABLE manager_orders
    ADD CHECK ( (SELECT u.role
                 FROM users u
                 WHERE u.id = manager_id) = 'MANAGER' );