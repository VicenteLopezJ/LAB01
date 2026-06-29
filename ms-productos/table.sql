CREATE TABLE IF NOT EXISTS productos(
  id BIGSERIAL primary key,
  name VARCHAR(60) not null,
  price decimal(10,2) not null,
  stock integer not null default 0,
  active boolean default true
);
CREATE TABLE IF NOT EXISTS pedidos(
  id BIGSERIAL PRIMARY KEY,
  product_id BIGINT NOT NULL,
  quantity INTEGER NOT NULL,
  total decimal(10,2) not null,
  status VARCHAR(20) default 'PENDING',
  date timestamp default now()
);
INSERT INTO productos (name, price, stock, active) values
('Laptop', 2500.00, 10, true),
('Mouse', 50.00, 40, true);
select * from productos;