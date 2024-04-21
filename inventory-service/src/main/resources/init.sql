create table inventory
(
    article_1c_id integer not null,
    product_id    integer not null unique ,
    quantity      integer not null,
    primary key (article_1c_id),
    constraint inventory_prod_id_fk foreign key
        (product_id) references product (id)
);

alter table inventory
    add constraint inventory_quant_not_negative check ( quantity >= 0 );