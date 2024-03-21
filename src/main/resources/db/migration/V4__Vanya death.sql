ALTER TABLE product
    ADD COLUMN article_1c_id integer not null;

ALTER TABLE product
    ADD CONSTRAINT product_art_id_uk UNIQUE (article_1c_id) ;
