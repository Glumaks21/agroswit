create table category
(
    id                 integer     not null auto_increment,
    logo               varchar(45),
    name               varchar(50) not null unique,
    description        varchar(300),
    parent_category_id integer,
    primary key (id),
    constraint category_prnt_cat_id
        foreign key (parent_category_id) references category (id),
    constraint category_name_parent_cat_id_uk unique (name, parent_category_id)
);

# create trigger category_parent_cat_id_trigger
#     after insert
#     on category
#     for each row
# begin
#     if NEW.parent_category_id = NEW.id then
#         signal sqlstate '45000' set message_text = 'parent_category_id cannot references itself';
#     end if;
# end;

create table category_properties
(
    id          integer      not null auto_increment,
    category_id integer      not null,
    name        varchar(255) not null,
    primary key (id),
    constraint category_properties_cat_id_fk
        foreign key (category_id) references category (id),
    constraint cat_prop_name_cat_id_uk unique (name, category_id)
);

create table producer
(
    id   integer      not null auto_increment,
    logo varchar(45) unique,
    name varchar(100) not null unique,
    primary key (id)
);

create table product
(
    id            integer                            not null auto_increment,
    image         varchar(45) unique,
    name          varchar(30)                        not null,
    description   varchar(500),
    active        bit                                not null default true,
    volume        integer                            not null,
    unit          enum ('BAG', 'BIG_BAG', 'KG', 'L') not null,
    producer_id   integer,
    category_id   integer,
    article_1c_id integer unique,
    primary key (id),
    constraint product_prod_id_fk
        foreign key (producer_id)
            references producer (id) ON DELETE SET NULL ,
    constraint product_cat_id_fk
        foreign key (category_id)
            references category (id) ON DELETE SET NULL

);

create table product_properties
(
    id         integer      not null auto_increment,
    product_id integer      not null,
    name       varchar(50)  not null,
    value      varchar(255) not null,
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

alter table product_packages
    add constraint product_pckgs_price_constr check ( price > 0 );
alter table product_packages
    add constraint product_pckgs_count_constr check ( count > 0 );