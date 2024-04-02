INSERT INTO producer(name, logo_url)
VALUES ('Agronutrition', 'https://agroswit.com.ua/image/cache/catalog/Parthners/09-agronutrition-100x100.png'),
       ('FMC Ukraine', 'https://agroswit.com.ua/image/cache/catalog/Parthners/04-fmc-100x100.png'),
       ('Corteva Agriscience', 'https://agroswit.com.ua/image/cache/catalog/Parthners/03-corteva-100x100.png'),
       ('MAS Seeds', 'https://agroswit.com.ua/image/cache/catalog/Parthners/23-maïsadour_semences-100x100.png'),
       ('UPL', 'https://agroswit.com.ua/image/cache/catalog/Parthners/05-upl-100x100.png'),
       ('May Seeds', 'https://agroswit.com.ua/image/cache/catalog/Parthners/06-may-100x100.png'),
       ('Bayer', 'https://agroswit.com.ua/image/cache/catalog/Parthners/01-bayer-100x100.png');

INSERT INTO category(name)
VALUES ('Засоби захисту рослин'),
       ('Насіння'),
       ('Мікродобрива');

INSERT INTO category(name, parent_category_id)
VALUES ('Гербіциди', 1),
       ('Фунгіциди', 1),
       ('Соняшник', 2),
       ('Кукуруза', 2),
       ('Рідкі мікродобрива', 3),
       ('Сухі мікродобрива', 3);

INSERT INTO category_property(name, type, category_id)
VALUES ('олійність', 'TEXT', 6),
       ('висота', 'NUMBER', 7),
       ('місце збирання', 'TEXT', 6),
       ('група стиглосты', 'TEXT', 6),
       ('система гербіцидного захисту', 'TEXT', 6),
       ('рекомендована зона культивації', 'TEXT', 6),
       ('випадки використання', 'TEXT', 6),
       ('супротив до вовчка', 'TEXT', 6),
       ('збирання', 'TEXT', 6);

INSERT INTO product(name, image_url, producer_id, category_id, article_1c_id)
VALUES ('Рейна', 'https://agroswit.com.ua/image/cache/catalog/Seeds/01-reyna-500x500.png', 6, 6, 1),
       ('ДуетКЛ', 'https://agroswit.com.ua/image/cache/catalog/Seeds/04-duet-500x500.png', 6, 6, 2),
       ('Еленіс', 'https://agroswit.com.ua/image/cache/catalog/Seeds/55-elenis-1-500x500.png', 7, 6, 3),
       ('Харнес®, КЕ', 'https://agroswit.com.ua/image/cache/catalog/ZZR/02-harnes-500x500.png', 7, 4, 4);

INSERT INTO package(price, product_id, volume, unit)
VALUES (5000, 1, 100, 'BAG'),
       (6000, 2, 100, 'BAG'),
       (8000, 3, 100, 'BAG'),
       (6400, 4, 100, 'BAG');

INSERT INTO product_property_value(product_id, property_id, prop_value)
VALUES (1, 1, '48-52'),
       (1, 3, '50тис.'),
       (1, 6, 'степ, лісостеп'),
       (1, 8, 'до 8 рас');

INSERT INTO users(email, phone, password, role)
VALUES ('glumaks21@gmail.com', '0961285462', '$2a$10$8uyWkYjmBFy.Rk9N6iQJDet0JF01EhxubbelUOGLF.KVs9BSnWj5C', 'ADMIN'),
       ('customer@gmail.com', '0961285461', 'qwerty', 'USER');

INSERT INTO customer(user_id, type)
VALUES ( 2, 'PERSON' );

INSERT INTO person_details(customer_id, name, surname, patronymic)
VALUES ( 1, 'Михайло', 'Недобіткін', 'Вікторович');

INSERT INTO order_state(name)
VALUES ('RECEIVED'),
       ('PROCEED'),
       ('REVIEW'),
       ('ACCEPTED'),
       ('ABORTED');

INSERT INTO orders(state, customer_id)
VALUES ('RECEIVED', 1);

INSERT INTO order_items(order_id, product_id, package_id, count)
VALUES (1, 1, 1, 10);