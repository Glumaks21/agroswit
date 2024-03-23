create table producer
(
    id       integer not null auto_increment,
    logo_url varchar(255) not null,
    name     varchar(255) not null,
    primary key (id)
) engine=InnoDB;

alter table product
    add column producer_id integer;

alter table producer
    add constraint producer_logo_uk unique (logo_url);
alter table producer
    add constraint producer_name_uk unique (name);

alter table product
    add constraint product_producer_id_fk
        foreign key (producer_id) references producer(id);
