INSERT INTO producer(name, logo)
VALUES ('Agronutrition', '12c39d73-54d5-424f-8050-b7e2a3a826b1.png'),
       ('FMC Ukraine', 'b0f493d1-292c-450d-8aaf-efb7f154dac3.png'),
       ('Corteva Agriscience', '2e677cd1-163c-4a46-afce-ce7d0d9255be.png'),
       ('MAS Seeds', 'a9cd954c-c650-4bdd-9dfa-5c2de8738da7.png'),
       ('UPL', 'ff43b16d-d417-4edb-a054-50f409d73f62.png'),
       ('May Seeds', '366a719b-63b6-4976-b529-a513608d2447.png'),
       ('Bayer', '2a7a1133-f7f2-4643-bf9c-02636ee6eeb1.png');

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

INSERT INTO category_properties(category_id, name)
VALUES (3, 'олійність'),
       (4, 'висота'),
       (3, 'місце збирання'),
       (3, 'група стиглосты'),
       (3, 'система гербіцидного захисту'),
       (3, 'рекомендована зона культивації'),
       (3, 'випадки використання'),
       (3, 'супротив до вовчка'),
       (3, 'збирання');

INSERT INTO product(name, producer_id, volume, unit, category_id, article_1c_id)
VALUES ('Рейна', 6, 100, 'BAG', 6, 1),
       ('ДуетКЛ', 6, 300, 'BAG', 6, 2),
       ('Еленіс', 7, 100, 'BAG', 6, 3),
       ('Харнес®, КЕ', 7, 150, 'BAG', 4, 4);

INSERT INTO inventory(product_1с_id, quantity)
VALUES (1, 123),
       (2, 323),
       (3, 15),
       (4, 54);

INSERT INTO product_packages(product_id, price, count)
VALUES (1, 5000, 1000),
       (2, 6000, 2000),
       (2, 6000, 4000),
       (2, 6000, 6000),
       (3, 8000, 3000),
       (4, 6400, 2000);

INSERT INTO product_properties(product_id, name, value)
VALUES (1, 'олійність', '48-52'),
       (1, 'місце збирання', '50тис.'),
       (1, 'рекомендована зона культивації', 'степ, лісостеп'),
       (1, 'збирання', 'до 8 рас');

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

INSERT INTO order_items(order_id, package_id, price, quantity)
VALUES (1, 1, 300, 10);