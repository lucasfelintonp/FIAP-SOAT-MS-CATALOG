package br.com.fiap.fastfood.inventory.domain.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InventoryProductsEntityTest {

    private InventoryEntity inventory;
    private UUID productId;
    private UUID id;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        productId = UUID.randomUUID();

        UnitEntity unit = new UnitEntity(1, "Unidade", "UN");
        inventory = new InventoryEntity(
            UUID.randomUUID(), "Produto Base", unit, BigDecimal.TEN, BigDecimal.ONE, null, null, null
        );
    }

    @Test
    @DisplayName("Deve arredondar a quantidade para 2 casas decimais no construtor")
    void constructor_shouldRoundQuantity() {
        var qty = new BigDecimal("10.555");
        var entity = new InventoryProductsEntity(id, productId, inventory, qty, null, null);

        assertEquals(new BigDecimal("10.56"), entity.getQuantity());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1000.00", "999.995", "1234.5"})
    @DisplayName("Deve lançar exceção quando a precisão exceder 5 dígitos")
    void constructor_shouldThrowExceptionWhenPrecisionTooHigh(String invalidQty) {
        assertThrows(IllegalArgumentException.class, () ->
            new InventoryProductsEntity(id, productId, inventory, new BigDecimal(invalidQty), null, null)
        );
    }

    @Test
    @DisplayName("Deve lançar exceção para campos obrigatórios nulos")
    void constructor_shouldThrowExceptionForNullFields() {
        assertThrows(IllegalArgumentException.class, () ->
            new InventoryProductsEntity(id, null, inventory, BigDecimal.ONE, null, null));

        assertThrows(IllegalArgumentException.class, () ->
            new InventoryProductsEntity(id, productId, null, BigDecimal.ONE, null, null));
    }

    @Test
    @DisplayName("Deve atualizar quantidade com arredondamento via setter")
    void setQuantity_shouldRoundValue() {
        var entity = new InventoryProductsEntity(id, productId, inventory, BigDecimal.ONE, null, null);
        entity.setQuantity(new BigDecimal("5.4321"));

        assertEquals(new BigDecimal("5.43"), entity.getQuantity());
        assertNotNull(entity.getUpdatedAt());
    }
}
