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

INSERT INTO subcategory(name, category_id)
VALUES ('Гербіциди', 1),
       ('Фунгіциди', 1),
       ('Соняшник', 2),
       ('Кукуруза', 2),
       ('Рідкі мікродобрива', 3),
       ('Сухі мікродобрива', 3);

INSERT INTO subcategory_property(name, type, subcategory_id)
VALUES ('олійність', 'TEXT', 3),
       ('висота', 'NUMBER', 4),
       ('місце збирання', 'TEXT', 3),
       ('група стиглосты', 'TEXT', 3),
       ('система гербіцидного захисту', 'TEXT', 3),
       ('рекомендована зона культивації', 'TEXT', 3),
       ('випадки використання', 'TEXT', 3),
       ('супротив до вовчка', 'TEXT', 3),
       ('збирання', 'TEXT', 3);

INSERT INTO product(name, price, producer_id, subcategory_id, article_1c_id)
VALUES ('Рейна', 5000, 6, 3, 1),
       ('ДуетКЛ', 6000, 6, 3, 2),
       ('Еленіс', 8000, 7, 3, 3),
       ('Харнес®, КЕ', 6400, 7, 1, 4);

INSERT INTO product_property_value(product_id, property_id, prop_value)
VALUES (1, 1, '48-52'),
       (1, 3, '50тис.'),
       (1, 6, 'степ, лісостеп'),
       (1, 8, 'до 8 рас');

INSERT INTO users(login, password, role)
VALUES ( 'glumaks21', '$2a$10$8uyWkYjmBFy.Rk9N6iQJDet0JF01EhxubbelUOGLF.KVs9BSnWj5C', 'ADMIN' );