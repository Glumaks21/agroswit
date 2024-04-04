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

create table category_property
(
    id          integer      not null auto_increment,
    type        enum ('TEXT', 'NUMBER') not null,
    name        varchar(255) not null,
    category_id integer      not null,
    primary key (id),
    constraint category_property_cat_id_fk
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
    id            integer     not null auto_increment,
    image_url     varchar(255),
    name          varchar(30) not null unique,
    description   varchar(500),
    volume        integer     not null,
    unit          enum ('BAG', 'BIG_BAG', 'SEEDS', 'KG', 'L') not null,
    producer_id   integer,
    category_id   integer     not null,
    article_1c_id integer unique,
    primary key (id),
    constraint product_prod_id_fk
        foreign key (producer_id)
            references producer (id),
    constraint product_cat_id_fk
        foreign key (category_id)
            references category (id)

);

create table package
(
    id         integer   not null auto_increment,
    product_id integer   not null,
    price      float(53) not null,
    count      integer   not null,
    primary key (id)
);

create table product_property_value
(
    product_id  integer      not null,
    property_id integer      not null,
    prop_value  varchar(255) not null,
    primary key (product_id, property_id)
);

create table inventory
(
    product_id integer not null,
    quantity   integer not null,
    primary key (product_id),
    constraint inventory_prod_id foreign key
        (product_id) references product (id)
)