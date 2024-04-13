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
VALUES (6, 'олійність'),
       (7, 'висота'),
       (6, 'місце збирання'),
       (6, 'група стиглосты'),
       (6, 'система гербіцидного захисту'),
       (6, 'рекомендована зона культивації'),
       (6, 'випадки використання'),
       (6, 'супротив до вовчка'),
       (6, 'збирання');

INSERT INTO product(name, producer_id, volume, unit, category_id, article_1c_id)
VALUES ('Рейна', 6, 100, 'BAG', 6, 1),
       ('ДуетКЛ', 6, 300, 'BAG', 6, 2),
       ('Еленіс', 7, 100, 'BAG', 6, 3),
       ('Харнес®, КЕ', 7, 150, 'BAG', 4, 4);