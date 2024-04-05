create table order_state
(
    name varchar(30) not null,
    primary key (name)
);

create table orders
(
    id          integer     not null auto_increment,
    state       varchar(30) not null references order_state (name),
    created_at  datetime(6),
    customer_id integer     not null,
    primary key (id),
    constraint orders_user_id_fk
        foreign key (customer_id) references customer (id)
);

create table order_items
(
    id         integer   not null auto_increment,
    order_id   integer   not null,
    product_id integer   not null,
    price      float(53) not null,
    quantity   integer   not null,
    primary key (id),
    constraint order_item_ordr_id_fk
        foreign key (order_id) references orders (id),
    constraint order_item_prod_id_fk
        foreign key (product_id) references product (id)
);

create table order_history
(
    order_id    integer     not null,
    state       varchar(30) not null references order_state (name),
    modified_at timestamp,
    user_id     integer     not null,
    primary key (order_id),
    constraint orders_history_user_id_fk
        foreign key (user_id) references users (id)
);
