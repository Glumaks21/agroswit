create table orders
(
    id          integer not null auto_increment,
    state       enum ('CREATED', 'PROCEED', 'REVIEW', 'ACCEPTED', 'ABORTED'),
    created_at  datetime(6),
    customer_id integer not null,
    primary key (id),
    constraint orders_user_id_fk
        foreign key (customer_id) references customer (user_id)
);

create table order_items
(
    id         integer   not null auto_increment,
    order_id   integer   not null,
    package_id integer,
    price      float(53) not null,
    count      integer   not null,
    primary key (id),
    constraint order_item_ordr_id_fk
        foreign key (order_id) references orders (id),
    constraint order_item_pckg_id_fk
        foreign key (package_id) references product_packages (id)
);

create table order_history
(
    id          integer not null auto_increment,
    order_id    integer not null,
    state       enum ('CREATED', 'PROCEED', 'REVIEW', 'ACCEPTED', 'ABORTED'),
    description varchar(100),
    modified_at timestamp,
    changed_by  integer not null,
    primary key (id),
    constraint orders_history_user_id_fk
        foreign key (changed_by) references users (id)
);
