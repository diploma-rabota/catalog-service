create index if not exists idx_product_price
    on product (price);

create index if not exists idx_product_rating
    on product (rating);

create index if not exists idx_product_weekly_growth_percent
    on product (weekly_growth_percent);

create index if not exists idx_product_active_category
    on product (is_active, category_id);

create index if not exists idx_product_active_supplier
    on product (is_active, supplier_id);

create index if not exists idx_product_stock_quantity
    on product (stock_quantity);


--category

insert into category (name, description)
select
    'Категория ' || gs,
    'Описание категории ' || gs
from generate_series(1, 50) gs
on conflict do nothing;


---postvchik

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
select
    'Поставщик ' || gs,
    'Описание поставщика ' || gs,
    round((3.5 + random() * 1.5)::numeric, 1),
    'https://example.com/images/suppliers/supplier-' || gs || '.jpg',
    case
        when gs % 5 = 0 then 'Лидер'
        when gs % 7 = 0 then 'Популярный'
        when gs % 11 = 0 then 'Надежный'
        else null
        end,
    (100 + (random() * 5000)::int),
    round((100000 + random() * 50000000)::numeric, 2),
    (-20 + (random() * 120)::int),
    true
from generate_series(1, 300) gs
on conflict do nothing;


----products

with category_ids as (
    select array_agg(id) as ids
    from category
),
     supplier_ids as (
         select array_agg(id) as ids
         from supplier
     )
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
select
    'Товар ' || gs,
    'Описание товара ' || gs,
    round((500 + random() * 150000)::numeric, 2),
    round((300 + random() * 120000)::numeric, 2),
    greatest(1, (random() * 20)::int),
    (random() * 500)::int,
    case when random() > 0.05 then true else false end,
    cat.ids[1 + floor(random() * array_length(cat.ids, 1))::int],
    'GEN-' || gs,
    'https://example.com/images/products/product-' || gs || '.jpg',
    round((3.0 + random() * 2.0)::numeric, 1),
    (-10 + (random() * 150)::int),
    sup.ids[1 + floor(random() * array_length(sup.ids, 1))::int]
from generate_series(1, 30000) gs
         cross join category_ids cat
         cross join supplier_ids sup
on conflict do nothing;