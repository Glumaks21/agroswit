create table category
(
    id                 integer     not null auto_increment,
    parent_category_id integer,
    name               varchar(30) not null unique,
    description        varchar(300),
    primary key (id),
    constraint category_prnt_cat_id
        foreign key (parent_category_id) references category (id),
    constraint category_name_parent_cat_id_uk unique (name, parent_category_id)
);

create table category_properties
(
    id          integer                 not null auto_increment,
    category_id integer                 not null,
    type        enum ('TEXT', 'NUMBER') not null,
    name        varchar(255)            not null,
    primary key (id),
    constraint category_properties_cat_id_fk
        foreign key (category_id) references category (id),
    constraint cat_prop_name_cat_id_uk unique (name, category_id)
);

create table producer
(
    id       integer      not null auto_increment,
    logo_url varchar(255) not null unique,
    name     varchar(255) not null unique,
    primary key (id)
);

create table product
(
    id            integer                                     not null auto_increment,
    image         varchar(255),
    name          varchar(30)                                 not null unique,
    description   varchar(500),
    active        bit                                         not null default true,
    volume        integer                                     not null,
    unit          enum ('BAG', 'BIG_BAG', 'SEEDS', 'KG', 'L') not null,
    producer_id   integer,
    category_id   integer                                     not null,
    article_1c_id integer unique,
    primary key (id),
    constraint product_prod_id_fk
        foreign key (producer_id)
            references producer (id),
    constraint product_cat_id_fk
        foreign key (category_id)
            references category (id)

);

create table product_properties
(
    id         integer                 not null auto_increment,
    product_id integer                 not null,
    name       varchar(50)             not null,
    type       enum ('TEXT', 'NUMBER') not null,
    value      varchar(255)            not null,
    primary key (id),
    constraint product_properties_prod_id_fk foreign key
        (product_id) references product (id)
);

create table product_packages
(
    id         integer   not null auto_increment,
    product_id integer   not null,
    price      float(53) not null,
    count      integer   not null,
    primary key (id),
    constraint product_packages foreign key
        (product_id) references product (id)
);

create table inventory
(
    product_1с_id integer not null,
    quantity      integer not null,
    primary key (product_1с_id),
    constraint inventory_prod_1с_id foreign key
        (product_1с_id) references product (article_1c_id)
);