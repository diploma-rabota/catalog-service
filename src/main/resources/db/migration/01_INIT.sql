create sequence if not exists sqn_category
    start with 1
    increment by 1;

create sequence if not exists sqn_product
    start with 1
    increment by 1;

create table if not exists category
(
    id          bigint primary key default nextval('sqn_category'),
    name        varchar(150) not null,
    description varchar(500)
    );

create table if not exists product
(
    id                      bigint primary key default nextval('sqn_product'),
    name                    varchar(200) not null,
    description             varchar(1000),
    price                   numeric(12, 2) not null,
    wholesale_price         numeric(12, 2) not null,
    min_wholesale_quantity  integer not null,
    stock_quantity          integer not null,
    is_active               boolean not null default true,
    category_id             bigint not null,

    constraint fk_product_category
    foreign key (category_id)
    references category (id),

    constraint chk_product_price
    check (price >= 0),

    constraint chk_product_wholesale_price
    check (wholesale_price >= 0),

    constraint chk_product_min_wholesale_quantity
    check (min_wholesale_quantity > 0),

    constraint chk_product_stock_quantity
    check (stock_quantity >= 0)
    );

create index if not exists idx_product_category_id
    on product (category_id);

create index if not exists idx_product_is_active
    on product (is_active);

comment on table category is 'Категории товаров';
comment on column category.id is 'Идентификатор категории';
comment on column category.name is 'Наименование категории';
comment on column category.description is 'Описание категории';

comment on table product is 'Товары каталога';
comment on column product.id is 'Идентификатор товара';
comment on column product.name is 'Наименование товара';
comment on column product.description is 'Описание товара';
comment on column product.price is 'Розничная цена товара';
comment on column product.wholesale_price is 'Оптовая цена товара';
comment on column product.min_wholesale_quantity is 'Минимальное количество для оптовой покупки';
comment on column product.stock_quantity is 'Доступное количество товара';
comment on column product.is_active is 'Признак активности товара';
comment on column product.category_id is 'Идентификатор категории товара';