#Managers
INSERT INTO manager (user_id, district)
VALUES
    (3, 'KYIV'),
    (6, 'LVIV'),
    (9, 'ODESA');

#Customers
INSERT INTO customer (user_id, type, district, region, settlement)
VALUES
    (12, 'PERSON', 'LVIV', 'Lviv Oblast', 'Lviv'),
    (13, 'ENTREPRENEUR', 'KHARKIV', 'Kharkiv Oblast', 'Kharkiv'),
    (14, 'COMPANY', 'KYIV', 'Kyiv Oblast', 'Kyiv'),
    (15, 'PERSON', 'ODESA', 'Odesa Oblast', 'Odesa'),
    (16, 'ENTREPRENEUR', 'DNIPROPETROVSK', 'Dnipropetrovsk Oblast', 'Dnipro'),
    (17, 'COMPANY', 'ZAPORIZHIA', 'Zaporizhzhia Oblast', 'Zaporizhzhia'),
    (18, 'PERSON', 'KYIV_CITY', 'Kyiv', 'Kyiv'),
    (19, 'ENTREPRENEUR', 'ZAKARPATTIA', 'Zakarpattia Oblast', 'Uzhhorod'),
    (20, 'COMPANY', 'LVIV', 'Lviv Oblast', 'Lviv'),
    (21, 'PERSON', 'CHERNIHIV', 'Chernihiv Oblast', 'Chernihiv');

#Persons
INSERT INTO person (customer_id, id_number, sex, birth_date)
VALUES
    (12, '1234567890', 'MALE', '1985-07-15'),
    (15, '0987654321', 'FEMALE', '1990-03-20'),
    (18, '5678901234', 'MALE', '1982-11-28'),
    (21, '3456789012', 'FEMALE', '1978-09-10');

#Entrepreneurs
INSERT INTO entrepreneur (customer_id, company_name, id_number)
VALUES
    (13, 'ABC Consulting', '9876543210'),
    (16, 'XYZ Solutions', '0123456789'),
    (19, 'DEF Enterprises', '5432109876');

#Companies
INSERT INTO company (customer_id, company_name, egrpou, incorporation_date)
VALUES
    (14, 'XYZ Corp', '123456789012', '2005-10-15'),
    (17, 'ABC Ltd', '987654321098', '2010-07-20'),
    (20, 'DEF Inc', '654321098765', '2018-04-05');