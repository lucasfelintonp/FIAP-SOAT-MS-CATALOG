-- Test data for BDD Category tests
-- This script ensures the categories referenced in CategoryTests.feature exist

-- Insert test categories if they don't exist
INSERT INTO product_category (id, name) VALUES (1, 'Lanche') ON CONFLICT (id) DO NOTHING;
INSERT INTO product_category (id, name) VALUES (2, 'Bebida') ON CONFLICT (id) DO NOTHING;
INSERT INTO product_category (id, name) VALUES (3, 'Sobremesa') ON CONFLICT (id) DO NOTHING;
INSERT INTO product_category (id, name) VALUES (4, 'Acompanhamentos') ON CONFLICT (id) DO NOTHING;

