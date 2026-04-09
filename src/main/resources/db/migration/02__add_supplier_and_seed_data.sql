create sequence if not exists sqn_supplier
    start with 1
    increment by 1;

create table if not exists supplier
(
    id              bigint primary key default nextval('sqn_supplier'),
    name            varchar(200) not null,
    description     varchar(1000),
    rating          numeric(2, 1) not null default 0.0,
    image_url       varchar(500),
    badge           varchar(100),
    sales_count     integer not null default 0,
    revenue_amount  numeric(14, 2) not null default 0,
    growth_percent  integer not null default 0,
    is_active       boolean not null default true
    );

alter table product
    add column if not exists article varchar(100);

alter table product
    add column if not exists image_url varchar(500);

alter table product
    add column if not exists rating numeric(2, 1) not null default 0.0;

alter table product
    add column if not exists weekly_growth_percent integer not null default 0;

alter table product
    add column if not exists supplier_id bigint;

alter table product
    add constraint uq_product_article unique (article);

alter table product
    add constraint fk_product_supplier
        foreign key (supplier_id)
            references supplier (id);

create index if not exists idx_product_article
    on product (article);

create index if not exists idx_product_supplier_id
    on product (supplier_id);

comment on table supplier is 'Поставщики товаров';
comment on column supplier.id is 'Идентификатор поставщика';
comment on column supplier.name is 'Наименование поставщика';
comment on column supplier.description is 'Описание поставщика';
comment on column supplier.rating is 'Рейтинг поставщика';
comment on column supplier.image_url is 'Ссылка на изображение поставщика';
comment on column supplier.badge is 'Маркер для витрины';
comment on column supplier.sales_count is 'Количество продаж';
comment on column supplier.revenue_amount is 'Общий объем выручки';
comment on column supplier.growth_percent is 'Рост за период';
comment on column supplier.is_active is 'Признак активности поставщика';

comment on column product.article is 'Артикул товара';
comment on column product.image_url is 'Ссылка на изображение товара';
comment on column product.rating is 'Рейтинг товара';
comment on column product.weekly_growth_percent is 'Рост популярности товара за неделю';
comment on column product.supplier_id is 'Идентификатор поставщика';

insert into category (name, description)
values
    ('Электроника', 'Электронные устройства и аксессуары'),
    ('Одежда', 'Одежда и текстиль'),
    ('Мебель', 'Мебель для офиса и бизнеса'),
    ('Инструменты', 'Инструменты и оборудование'),
    ('Продукты', 'Продукты питания'),
    ('Упаковка', 'Упаковочные материалы')
    on conflict do nothing;

insert into supplier (
    name,
    description,
    rating,
    image_url,
    badge,
    sales_count,
    revenue_amount,
    growth_percent,
    is_active
)
values
    (
        'ТехноПоставка',
        'Поставщик электроники и компьютерной техники',
        4.8,
        'https://example.com/images/suppliers/techno-postavka.jpg',
        'Лидер продаж',
        1245,
        12500000.00,
        52,
        true
    ),
    (
        'СветТорг',
        'Поставщик светотехнической продукции',
        4.5,
        'https://example.com/images/suppliers/svet-torg.jpg',
        'Быстрый рост',
        987,
        8900000.00,
        48,
        true
    ),
    (
        'ОфисТех',
        'Поставщик офисного оборудования',
        4.9,
        'https://example.com/images/suppliers/office-tech.jpg',
        'Высокий рейтинг',
        856,
        7200000.00,
        41,
        true
    ),
    (
        'МебельОпт',
        'Поставщик офисной мебели',
        4.6,
        'https://example.com/images/suppliers/mebel-opt.jpg',
        'Надежный партнер',
        734,
        6800000.00,
        38,
        true
    ),
    (
        'ТехАксессуары',
        'Поставщик аксессуаров и периферии',
        4.6,
        'https://example.com/images/suppliers/tech-accessories.jpg',
        'Популярный',
        623,
        5100000.00,
        35,
        true
    ),
    (
        'ГастроОпт',
        'Поставщик кофемашин и оборудования для офиса',
        4.7,
        'https://example.com/images/suppliers/gastro-opt.jpg',
        'Стабильный спрос',
        540,
        4300000.00,
        29,
        true
    )
    on conflict do nothing;

insert into product (
    name,
    description,
    price,
    wholesale_price,
    min_wholesale_quantity,
    stock_quantity,
    is_active,
    category_id,
    article,
    image_url,
    rating,
    weekly_growth_percent,
    supplier_id
)
values
    (
        'Ноутбук Dell Latitude 5520',
        'Ноутбук для офисной работы и бизнеса',
        85000.00,
        79000.00,
        1,
        15,
        true,
        (select id from category where name = 'Электроника'),
        'EL-1001',
        'https://example.com/images/products/dell-latitude-5520.jpg',
        4.8,
        45,
        (select id from supplier where name = 'ТехноПоставка')
    ),
    (
        'LED лампы для офиса',
        'Комплект светодиодных ламп для офисных помещений',
        1200.00,
        950.00,
        10,
        250,
        true,
        (select id from category where name = 'Электроника'),
        'EL-1002',
        'https://example.com/images/products/led-office-lamps.jpg',
        4.7,
        38,
        (select id from supplier where name = 'СветТорг')
    ),
    (
        'Принтер HP LaserJet Pro',
        'Лазерный принтер для офиса',
        45000.00,
        42000.00,
        1,
        22,
        true,
        (select id from category where name = 'Электроника'),
        'EL-1003',
        'https://example.com/images/products/hp-laserjet-pro.jpg',
        4.9,
        32,
        (select id from supplier where name = 'ОфисТех')
    ),
    (
        'Монитор Samsung 27',
        'Монитор для рабочего места 27 дюймов',
        28000.00,
        25500.00,
        2,
        40,
        true,
        (select id from category where name = 'Электроника'),
        'EL-1004',
        'https://example.com/images/products/samsung-monitor-27.jpg',
        4.8,
        28,
        (select id from supplier where name = 'ТехноПоставка')
    ),
    (
        'Офисный стул',
        'Эргономичный стул для офисного использования',
        12500.00,
        11300.00,
        2,
        35,
        true,
        (select id from category where name = 'Мебель'),
        'MB-2001',
        'https://example.com/images/products/office-chair.jpg',
        4.6,
        25,
        (select id from supplier where name = 'МебельОпт')
    ),
    (
        'Клавиатура механическая',
        'Механическая клавиатура для интенсивной работы',
        5500.00,
        4900.00,
        3,
        60,
        true,
        (select id from category where name = 'Электроника'),
        'EL-1005',
        'https://example.com/images/products/mechanical-keyboard.jpg',
        4.6,
        22,
        (select id from supplier where name = 'ТехАксессуары')
    ),
    (
        'Кофемашина для офиса',
        'Автоматическая кофемашина для офиса',
        35000.00,
        32000.00,
        1,
        12,
        true,
        (select id from category where name = 'Продукты'),
        'FD-3001',
        'https://example.com/images/products/office-coffee-machine.jpg',
        4.7,
        19,
        (select id from supplier where name = 'ГастроОпт')
    ),
    (
        'Рабочий стол офисный',
        'Офисный стол для рабочего места',
        18500.00,
        16800.00,
        2,
        18,
        true,
        (select id from category where name = 'Мебель'),
        'MB-2002',
        'https://example.com/images/products/office-desk.jpg',
        4.5,
        15,
        (select id from supplier where name = 'МебельОпт')
    )
    on conflict do nothing;

update product
set article = concat('PRD-', id)
where article is null;

alter table product
    alter column article set not null;

alter table product
    alter column supplier_id set not null;