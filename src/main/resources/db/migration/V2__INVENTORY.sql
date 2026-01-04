-- Tabela de unidade de medida
CREATE TABLE IF NOT EXISTS unit (
    id INTEGER PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    abbreviation VARCHAR(10) NOT NULL
);

-- Tabela de estoque
CREATE TABLE IF NOT EXISTS inventory (
    id UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    unit_id INTEGER NOT NULL,
    quantity NUMERIC(5, 2),
    minimum_quantity NUMERIC(5, 2) NOT NULL,
    notes VARCHAR(100),
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT fk_unit
    FOREIGN KEY (unit_id) REFERENCES unit(id)
);

-- Tabela de lotes do estoque
CREATE TABLE IF NOT EXISTS inventory_entry (
    id UUID PRIMARY KEY,
    inventory_id UUID NOT NULL,
    quantity NUMERIC(5, 2) NOT NULL,
    expiration_date DATE, entry_date DATE NOT NULL,
    CONSTRAINT fk_inventory
    FOREIGN KEY (inventory_id) REFERENCES inventory(id)
);

-- Tabela de relação entre produto e estoque
CREATE TABLE IF NOT EXISTS inventory_products (
    id UUID PRIMARY KEY,
    product_id UUID,
    inventory_id UUID,
    quantity NUMERIC(5, 2),
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE INDEX "idx_inventory_product_id" ON inventory_products (product_id);
CREATE INDEX "idx_inventory_id" ON inventory_products (inventory_id);

INSERT INTO unit (id, name, abbreviation) VALUES
(1, 'Quilograma', 'kg'),
(2, 'Grama', 'g'),
(3, 'Miligrama', 'mg'),
(4, 'Litro', 'L'),
(5, 'Mililitro', 'mL'),
(6, 'Unidade', 'un');


INSERT INTO inventory (id, name, unit_id, quantity, minimum_quantity, notes, created_at, updated_at) VALUES
('28f2df95-fa99-4a5a-b1d2-29b24442e8df', 'Molho Especial', 5, '900', 100, 'Molho da casa', NOW(), NOW()),
('49f4b818-5052-4bcc-a7f6-3cc0176ae134', 'Queijo', 1, '10', 1, 'Queijo fatiado', NOW(), NOW()),
('645efa7d-7999-4aa8-9281-b36b43f7312d', 'Casquinha', 6, '50', 10, 'Casquinha crocante', NOW(), NOW()),
('7e6acba8-a73d-4c0b-8949-e03539106ce7', 'Lata de Refrigerante', 6, '100', 20, 'Diversos sabores', NOW(), NOW()),
('817cad41-99f2-4ca5-b656-f22108a2ad47', 'Laranja', 6, '30', 3, 'Para suco espremido', NOW(), NOW()),
('83752bab-b8a9-4f14-8ea9-625aa4f4446a', 'Sorvete de Baunilha', 4, '10', 2, 'Base do sundae', NOW(), NOW()),
('8694c94f-9ee9-48c9-a1f9-9a1ebad92461', 'Granulado', 2, '500', 50, 'Cobertura adicional', NOW(), NOW()),
('892ec2ef-6d7c-4af0-83c4-3a6a585285b3', 'Alface', 2, '300', 30, 'Folhas frescas', NOW(), NOW()),
('93235f55-e075-49b3-bbb5-66d1d52302e8', 'Filé de Frango', 1, '50', 5, 'Empanado crocante', NOW(), NOW()),
('98292880-a6f2-439b-b0ca-9b7eae597f5a', 'Pão de Hambúrguer', 6, '300', 30, 'Usado para hambúrgueres', NOW(), NOW()),
('9f30426c-e9bf-4eae-880d-9afd982dfc36', 'Carne de Hambúrguer', 1, '50', 5, 'Carne bovina para hambúrguer', NOW(), NOW()),
('be0822ed-c5a3-4baf-ba6b-9148b8c0b7f6', 'Maionese', 1, '3', '0.3', 'Condimento', NOW(), NOW()),
('d8e4f6d0-a8f8-4839-a8ee-b380e5279539', 'Calda de Chocolate', 4, '3', '0.3', 'Cobertura', NOW(), NOW()),
('f31b1c68-dc71-4acb-9b24-4c94152f0f23', 'Copo Descartável 300ml', 4, '100', '10', 'Para servir o suco', NOW(), NOW());


DO $$
DECLARE
    product_id_1 UUID := 'b4c6d7f1-2b3c-4d5e-9f01-2345678901bc';
    product_id_2 UUID := 'a3b5f3e0-1a2b-4c3d-8e9f-1234567890ab';
    product_id_3 UUID := 'f809b1c5-6f70-8192-d345-6789012345f0';
    product_id_4 UUID := 'e7f9a0b4-5e6f-7081-c234-5678901234ef';
    product_id_5 UUID := 'd6e8f9a3-4d5e-6f70-b123-4567890123de';
    inventory_id_1 UUID := '892ec2ef-6d7c-4af0-83c4-3a6a585285b3';
    inventory_id_2 UUID := '83752bab-b8a9-4f14-8ea9-625aa4f4446a';
    inventory_id_3 UUID := '98292880-a6f2-439b-b0ca-9b7eae597f5a';
BEGIN
INSERT INTO inventory_products (id, product_id, inventory_id, quantity, created_at, updated_at) VALUES
    ('1c851c2f-80ea-44a0-acb9-23ba8c28f2ca', product_id_1, '93235f55-e075-49b3-bbb5-66d1d52302e8', 2.00, '2025-05-31 20:07:39.745935', '2025-05-31 20:07:39.745936'),
    ('213f29c7-d557-473c-8c17-48161790b5da', product_id_2, inventory_id_1, 0.50, '2025-05-31 20:07:09.172836', '2025-05-31 20:07:09.272836'),
    ('2438ee5a-a9fd-44c5-b34c-931189a999b5', product_id_1, inventory_id_3, 1.00, '2025-05-31 20:07:39.745937', '2025-05-31 20:07:39.745938'),
    ('2ea8d53e-b75a-4302-a6fb-b236a8ba80cc', product_id_1, inventory_id_1, 0.75, '2025-05-31 20:07:39.745939', '2025-05-31 20:07:39.745941'),
    ('37d37fd5-adbc-423e-b5ad-280ed3114670', product_id_3, inventory_id_2, 0.30, '2025-05-31 20:08:13.9095', '2025-05-31 20:08:13.9096'),
    ('3b09e24a-0803-4b4c-b984-8e3abb315d76', product_id_2, '49f4b818-5052-4bcc-a7f6-3cc0176ae134', 1.00, '2025-05-31 20:07:09.372836', '2025-05-31 20:07:09.472836'),
    ('42fb6518-5630-4d2c-ad3d-dcc477ef4ade', product_id_2, '28f2df95-fa99-4a5a-b1d2-29b24442e8df', 0.25, '2025-05-31 20:07:09.572836', '2025-05-31 20:07:09.672836'),
    ('4c0e8690-6bb9-45b9-8416-fde4ccac085a', product_id_2, '9f30426c-e9bf-4eae-880d-9afd982dfc36', 2.00, '2025-05-31 20:07:09.772836', '2025-05-31 20:07:09.872836'),
    ('5aa6201b-f2ab-40fa-828f-7311caa5ba65', product_id_4, inventory_id_2, 0.50, '2025-05-31 20:08:01.6232', '2025-05-31 20:08:02.6232'),
    ('85e9ffe7-acbb-441f-b031-16fe7beab279', product_id_4, '8694c94f-9ee9-48c9-a1f9-9a1ebad92461', 0.10, '2025-05-31 20:08:03.6232', '2025-05-31 20:08:04.6232'),
    ('997a40f2-b2fd-4f86-9f39-87822d01723b', product_id_5, 'f31b1c68-dc71-4acb-9b24-4c94152f0f23', 1.00, '2025-05-31 20:07:54.880593', '2025-05-31 20:07:54.880594'),
    ('9a01a5e8-7e07-46c5-b47a-65c96e3d7f7e', product_id_2, inventory_id_3, 1.50, '2025-05-31 20:07:09.972836', '2025-05-31 20:07:09.212836'),
    ('aab2f24b-46bf-4f41-8499-929f7364a1e3', product_id_4, 'd8e4f6d0-a8f8-4839-a8ee-b380e5279539', 0.20, '2025-05-31 20:08:04.5232', '2025-05-31 20:08:06.6232'),
    ('ba105eae-939d-481d-9a6b-1b75850ebe3f', 'c5d7e8f2-3c4d-5e6f-a012-3456789012cd', '7e6acba8-a73d-4c0b-8949-e03539106ce7', 1.00, '2025-05-31 20:07:47.645258', '2025-05-31 20:07:47.645258'),
    ('d12deb6b-e43b-496e-872c-42cd71e6a529', product_id_1, 'be0822ed-c5a3-4baf-ba6b-9148b8c0b7f6', 0.15, '2025-05-31 20:07:39.745942', '2025-05-31 20:07:39.745943'),
    ('d1d15f4e-0cc7-49ce-8fd0-9207b74521fd', product_id_5, '817cad41-99f2-4ca5-b656-f22108a2ad47', 0.80, '2025-05-31 20:07:54.880595', '2025-05-31 20:07:54.880596'),
    ('f44d6a04-c079-414a-ae1c-a7e67ef6f893', product_id_3, '645efa7d-7999-4aa8-9281-b36b43f7312d', 1.00, '2025-05-31 20:08:13.9097', '2025-05-31 20:08:13.9098');
END $$;
