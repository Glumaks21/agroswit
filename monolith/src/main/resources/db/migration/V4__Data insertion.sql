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

INSERT INTO category_properties(category_id, name, type)
VALUES (3, 'олійність', 'TEXT'),
       (4, 'висота', 'NUMBER'),
       (3, 'місце збирання', 'TEXT'),
       (3, 'група стиглосты', 'TEXT'),
       (3, 'система гербіцидного захисту', 'TEXT'),
       (3, 'рекомендована зона культивації', 'TEXT'),
       (3, 'випадки використання', 'TEXT'),
       (3, 'супротив до вовчка', 'TEXT'),
       (3, 'збирання', 'TEXT');

INSERT INTO product(name, image_url, producer_id, volume, unit, category_id, article_1c_id)
VALUES ('Рейна', 'https://agroswit.com.ua/image/cache/catalog/Seeds/01-reyna-500x500.png', 6, 100, 'BAG', 6, 1),
       ('ДуетКЛ', 'https://agroswit.com.ua/image/cache/catalog/Seeds/04-duet-500x500.png', 6, 300, 'BAG', 6, 2),
       ('Еленіс', 'https://agroswit.com.ua/image/cache/catalog/Seeds/55-elenis-1-500x500.png', 7, 100, 'BAG', 6, 3),
       ('Харнес®, КЕ', 'https://agroswit.com.ua/image/cache/catalog/ZZR/02-harnes-500x500.png', 7, 150, 'BAG', 4, 4);

INSERT INTO product_packages(product_id, price, count)
VALUES (1, 5000, 1000),
       (2, 6000, 2000),
       (3, 8000, 3000),
       (4, 6400, 2000);

INSERT INTO product_properties(product_id, name, type, value)
VALUES (1, 'олійність', 'TEXT', '48-52'),
       (1, 'місце збирання', 'TEXT', '50тис.'),
       (1, 'рекомендована зона культивації', 'TEXT', 'степ, лісостеп'),
       (1, 'збирання', 'TEXT', 'до 8 рас');

INSERT INTO users(email, phone, password, role)
VALUES ('glumaks21@gmail.com', '0961285462', '$2a$10$8uyWkYjmBFy.Rk9N6iQJDet0JF01EhxubbelUOGLF.KVs9BSnWj5C', 'ADMIN'),
       ('agroswit@test.com', '0961243854', '$2a$10$8uyWkYjmBFy.Rk9N6iQJDet0JF01EhxubbelUOGLF.KVs9BSnWj5C', 'USER'),
       ('customer@gmail.com', '0961285461', 'qwerty', 'USER');

INSERT INTO user_info(user_id, name, surname, patronymic)
VALUES (3, 'Михайло', 'Недобіткін', 'Вікторович');

INSERT INTO customer(user_id, type, region, district, settlement)
VALUES (2, 'COMPANY', 'Кіровоградська область', 'Олександрійський район', 'селище міського типу Приютівка'),
       (3, 'PERSON', 'Кіровоградська область', 'Олександрійський район', 'місто Олександрія');

INSERT INTO company_info(customer_id, name, egrpou, incorporation_date)
VALUES (1, 'ТОВ НВФ «АГРОСВІТ»', 23233729, '1997-07-07');

INSERT INTO person_info(customer_id, birth_date, sex)
VALUES (2, '2003-01-01', 0);

INSERT INTO order_state(name)
VALUES ('RECEIVED'),
       ('PROCEED'),
       ('REVIEW'),
       ('ACCEPTED'),
       ('ABORTED');

INSERT INTO orders(state, customer_id)
VALUES ('RECEIVED', 1);

INSERT INTO order_items(order_id, product_id, price, quantity)
VALUES (1, 1, 300, 10);