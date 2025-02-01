CREATE TABLE products (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    price DECIMAL(19,2) NOT NULL,
    stock_quantity INTEGER NOT NULL,
    sku VARCHAR(50) NOT NULL UNIQUE
); 