create table category
(
    id   integer not null auto_increment,
    name varchar(255) not null,
    primary key (id)
) engine=InnoDB;

create table subcategory
(
    id          integer not null auto_increment,
    name        varchar(255) not null,
    category_id integer      not null,
    primary key (id)
) engine=InnoDB;

create table subcategory_property
(
    id   integer not null auto_increment,
    name varchar(255) not null,
    subcategory_id integer not null,
    type enum ('TEXT','NUMBER') not null,
    primary key (id)
) engine=InnoDB;

create table product_property_value
(
    product_id  integer      not null,
    property_id integer      not null,
    prop_value  varchar(255) not null,
    primary key (product_id, property_id)
) engine=InnoDB;

alter table product
    add column subcategory_id integer not null default 1;

alter table category
    add constraint category_name_uk unique (name);

alter table subcategory
    add constraint subcat_name_cat_id_uk unique (name, category_id);

alter table subcategory
    add constraint subcategory_category_id_fk
        foreign key (category_id)
            references category(id);

alter table subcategory_property
    add constraint subcat_prop_name_subcat_id_uk unique (name, subcategory_id);

alter table subcategory_property
    add constraint subcat_props_subcat_id_fk
        foreign key (subcategory_id)
            references subcategory(id);

alter table product
    add constraint product_subcategory_id_fk
        foreign key (subcategory_id)
            references subcategory(id);
