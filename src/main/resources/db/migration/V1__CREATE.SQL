-- Tabela de categorias de produto
CREATE TABLE IF NOT EXISTS product_categories (
    id    SERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL
);

-- Tabela de produtos
CREATE TABLE IF NOT EXISTS products (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    price NUMERIC(5,2) NOT NULL,
    is_active BOOLEAN NOT NULL,
    image_path VARCHAR(250),
    category_id INT NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    deleted_at TIMESTAMP WITHOUT TIME ZONE,

    FOREIGN KEY (category_id) REFERENCES product_categories(id)
);

CREATE INDEX "idx_category_id" ON products (category_id);

INSERT INTO product_categories (name) VALUES
('Lanche'),
('Bebida'),
('Sobremesa'),
('Acompanhamentos');

INSERT INTO products (
    id, name, description, price, is_active, image_path, category_id, created_at, updated_at, deleted_at
) VALUES
-- Lanches
('a3b5f3e0-1a2b-4c3d-8e9f-1234567890ab', 'Cheeseburger', 'Hambúrguer com queijo, alface e molho especial', 19.90, true, '/images/cheeseburger.jpg', 1, now(), now(), NULL),
('b4c6d7f1-2b3c-4d5e-9f01-2345678901bc', 'Chicken Sandwich', 'Sanduíche de frango crocante com maionese e alface', 21.50, true, '/images/chicken_sandwich.jpg', 1, now(), now(), NULL),

-- Bebidas
('c5d7e8f2-3c4d-5e6f-a012-3456789012cd', 'Refrigerante 350ml', 'Lata de refrigerante à sua escolha', 6.00, true, '/images/soda.jpg', 2, now(), now(), NULL),
('d6e8f9a3-4d5e-6f70-b123-4567890123de', 'Suco Natural', 'Suco de laranja natural espremido na hora', 8.50, true, '/images/orange_juice.jpg', 2, now(), now(), NULL),

-- Sobremesas
('e7f9a0b4-5e6f-7081-c234-5678901234ef', 'Sundae Chocolate', 'Sundae com calda de chocolate e granulado', 9.90, true, '/images/sundae.jpg', 3, now(), now(), NULL),
('f809b1c5-6f70-8192-d345-6789012345f0', 'Casquinha de Baunilha', 'Sorvete de baunilha na casquinha crocante', 4.50, true, '/images/vanilla_cone.jpg', 3, now(), now(), NULL);
