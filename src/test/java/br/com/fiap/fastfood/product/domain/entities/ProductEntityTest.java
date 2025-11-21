package br.com.fiap.fastfood.product.domain.entities;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductEntityTest {

    @Test
    void recordAccessorsAndGetActive() {
        UUID id = UUID.fromString("e7f9a0b4-5e6f-7081-c234-5678901234ef");
        ProductEntity p = new ProductEntity(
            id,
            "Sundae Chocolate",
            "Sundae com calda de chocolate e granulado",
            new BigDecimal("9.90"),
            true,
            "/images/sundae.jpg",
            3,
            null,
            null,
            null
        );

        assertEquals(id, p.id());
        assertEquals("Sundae Chocolate", p.name());
        assertTrue(p.getActive());
    }

    @Test
    void equalityAndToString() {
        ProductEntity p1 = new ProductEntity(
            null,
            "Casquinha de Baunilha",
            "Sorvete de baunilha na casquinha crocante",
            new BigDecimal("4.50"),
            true,
            "/images/vanilla_cone.jpg",
            3,
            null,
            null,
            null
        );
        ProductEntity p2 = new ProductEntity(
            null,
            "Casquinha de Baunilha",
            "Sorvete de baunilha na casquinha crocante",
            new BigDecimal("4.50"),
            true,
            "/images/vanilla_cone.jpg",
            3,
            null,
            null,
            null
        );

        assertEquals(p1, p2);
        assertNotNull(p1.toString());
    }
}
