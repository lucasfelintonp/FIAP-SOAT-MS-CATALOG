package br.com.fiap.fastfood.category.domain.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductCategoryEntityTest {

    @Test
    void placeholder() {
        assertTrue(true);
    }

    @Test
    void recordAccessors() {
        ProductCategoryEntity e = new ProductCategoryEntity(1, "Lanche");
        assertEquals(1, e.id());
        assertEquals("Lanche", e.name());
    }
}
