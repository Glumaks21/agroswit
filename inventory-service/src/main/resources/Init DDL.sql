create table inventory
(
    product_1с_id integer not null,
    quantity      integer not null,
    primary key (product_1с_id),
    constraint inventory_prod_1с_id foreign key
        (product_1с_id) references product (article_1c_id)
);

alter table inventory
    add constraint inventory_quant_not_negative check ( quantity >= 0 );