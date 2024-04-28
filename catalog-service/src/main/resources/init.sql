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

create table category_property_group
(
    id          integer     not null auto_increment,
    category_id integer     not null,
    name        varchar(50) not null,
    primary key (id),
    constraint cat_prop_group_cat_id_fk
        foreign key (category_id)
            references category (id),
    constraint cat_prop_group_name_cat_id_uk unique (name, category_id)
);

create table category_property
(
    group_id integer     not null,
    name     varchar(50) not null,
    constraint cat_prop_group_id_uk
        foreign key (group_id)
            references category_property_group (id),
    constraint cat_prop_name_gr_id_uk unique (name, group_id)
);

create table producer
(
    id          integer     not null auto_increment,
    logo        varchar(45) unique,
    name        varchar(50) not null unique,
    description varchar(100),
    primary key (id)
);

create table product
(
    id                integer                  not null auto_increment,
    image             varchar(45) unique,
    name              varchar(50)              not null,
    type              enum ('PESTICIDE'),
    short_description varchar(100),
    full_description  varchar(1000),
    recommendations   varchar(400),
    active            bit                      not null default true,
    volume            integer                  not null,
    unit              enum ('SEED', 'KG', 'LITER') not null,
    producer_id       integer,
    category_id       integer,
    created_at        datetime                 not null,
    modified_at       datetime                 not null,
    primary key (id),
    constraint product_prod_id_fk
        foreign key (producer_id)
            references producer (id) ON DELETE SET NULL,
    constraint product_cat_id_fk
        foreign key (category_id)
            references category (id) ON DELETE SET NULL
);

create table product_property_group
(
    id         integer     not null auto_increment,
    name       varchar(50) not null,
    product_id integer     not null,
    primary key (id),
    constraint prod_prop_group_prod_id_fk foreign key
        (product_id) references product (id),
    constraint prod_prop_group_name_prod_id_uk unique (product_id, name)
);

create table product_property
(
    group_id integer      not null,
    name     varchar(50)  not null,
    value    varchar(255) not null,
    constraint product_property_group_id_fk foreign key
        (group_id) references product_property_group (id),
    constraint product_prop_group_name_uk unique (group_id, name)
);

create table product_packages
(
    id         integer   not null auto_increment,
    product_id integer   not null,
    price      float(53) not null,
    old_price  float(53) not null,
    count      integer   not null,
    primary key (id),
    constraint prod_pckgs_prod_id_fk foreign key
        (product_id) references product (id),
    constraint prod_pkcgs_count_per_pckg_uk unique (product_id, count)
);

create table filters
(
    id              integer      not null auto_increment,
    name            varchar(100) not null unique,
    filter_group_id integer,
    primary key (id),
    constraint filters_group_filt_id
        foreign key (filter_group_id)
            references filters (id) ON DELETE CASCADE
);

create table product_filters
(
    product_id integer not null,
    filter_id  integer not null,
    primary key (product_id, filter_id),
    constraint product_filters_prod_id_fk
        foreign key (product_id)
            references product (id),
    constraint product_filters_filter_id_fk
        foreign key (filter_id)
            references filters (id)
);

create table category_filters
(
    category_id integer not null,
    filter_id   integer not null,
    primary key (category_id, filter_id),
    constraint category_filters_cat_id_fk
        foreign key (category_id)
            references category (id),
    constraint category_filters_filter_id_fk
        foreign key (filter_id)
            references filters (id)
);

create table culture
(
    id   integer     not null auto_increment,
    name varchar(50) not null unique,
    primary key (id)
);

create table pest
(
    id   integer     not null auto_increment,
    name varchar(50) not null unique,
    primary key (id)
);

create table pesticide
(
    product_id int not null,
    primary key (product_id),
    constraint pesticide_prod_id_fk foreign key (product_id)
        references product (id)
);

create table pesticide_cultures
(
    pesticide_id integer not null,
    culture_id   integer not null,
    min_volume   double  not null,
    max_volume   double  not null,
    primary key (pesticide_id, culture_id),
    constraint pesticide_cults_pr_id_fk
        foreign key (pesticide_id)
            references pesticide (product_id),
    constraint pesticide_cultures_cul_id_fk
        foreign key (culture_id)
            references culture (id)
);

create table pesticide_pests
(
    pesticide_id integer not null,
    pest_id      integer not null,
    primary key (pesticide_id, pest_id),
    constraint pesticide_pests_pr_id_fk
        foreign key (pesticide_id)
            references pesticide (product_id),
    constraint pesticide_pests_pest_id_fk
        foreign key (pest_id)
            references pest (id)
);

alter table product_packages
    add constraint product_pckgs_price_constr check ( price > 0 );
alter table product_packages
    add constraint product_pckgs_count_constr check ( count > 0 );
alter table pesticide_cultures
    add constraint pesticide_cults_min_vol_constr check ( min_volume > 0 );
alter table pesticide_cultures
    add constraint pesticide_cults_max_vol_constr check ( max_volume > min_volume );