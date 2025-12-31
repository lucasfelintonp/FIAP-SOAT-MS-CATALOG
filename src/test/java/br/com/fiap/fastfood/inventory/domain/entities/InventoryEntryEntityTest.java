package br.com.fiap.fastfood.inventory.domain.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InventoryEntryEntityTest {

    private InventoryEntity inventory;
    private UUID id;
    private LocalDate entryDate;
    private LocalDate expirationDate;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        entryDate = LocalDate.now();
        expirationDate = LocalDate.now().plusMonths(6);

        UnitEntity unit = new UnitEntity(1, "Quilograma", "KG");
        inventory = new InventoryEntity(
            UUID.randomUUID(), "Carne", unit, BigDecimal.TEN, BigDecimal.ONE, null, null, null
        );
    }

    @Test
    @DisplayName("Deve criar uma entrada de estoque com sucesso quando os dados são válidos")
    void constructor_success_shouldCreateEntity() {
        var quantity = new BigDecimal("15.555");

        var entity = new InventoryEntryEntity(id, inventory, quantity, expirationDate, entryDate);

        assertEquals(id, entity.getId());
        assertEquals(inventory, entity.getInventory());
        assertEquals(new BigDecimal("15.56"), entity.getQuantity());
        assertEquals(expirationDate, entity.getExpirationDate());
        assertEquals(entryDate, entity.getEntryDate());
    }

    @Test
    @DisplayName("Deve gerar um novo UUID quando o ID fornecido for nulo")
    void constructor_success_shouldGenerateIdWhenNull() {
        var entity = new InventoryEntryEntity(null, inventory, BigDecimal.ONE, null, entryDate);
        assertNotNull(entity.getId());
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar entrada com inventário nulo")
    void constructor_error_nullInventory_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () ->
            new InventoryEntryEntity(id, null, BigDecimal.ONE, expirationDate, entryDate)
        );
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar entrada com quantidade nula")
    void constructor_error_nullQuantity_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () ->
            new InventoryEntryEntity(id, inventory, null, expirationDate, entryDate)
        );
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar entrada com data de entrada nula")
    void constructor_error_nullEntryDate_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () ->
            new InventoryEntryEntity(id, inventory, BigDecimal.ONE, expirationDate, null)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"1000.00", "1234.56", "999.995", "10000"})
    @DisplayName("Deve lançar exceção quando a quantidade excede a precisão permitida (mais de 5 dígitos)")
    void constructor_error_invalidQuantityPrecision_shouldThrowException(String qty) {
        BigDecimal invalidQty = new BigDecimal(qty);

        assertThrows(IllegalArgumentException.class, () ->
            new InventoryEntryEntity(id, inventory, invalidQty, expirationDate, entryDate)
        );
    }

    @Test
    @DisplayName("Deve atualizar a quantidade respeitando o arredondamento")
    void setQuantity_success_shouldUpdateAndFormat() {
        var entity = new InventoryEntryEntity(id, inventory, BigDecimal.ONE, null, entryDate);
        entity.setQuantity(new BigDecimal("20.123"));

        assertEquals(new BigDecimal("20.12"), entity.getQuantity());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar setar quantidade nula")
    void setQuantity_error_nullValue_shouldThrowException() {
        var entity = new InventoryEntryEntity(id, inventory, BigDecimal.ONE, null, entryDate);
        assertThrows(IllegalArgumentException.class, () -> entity.setQuantity(null));
    }

    @Test
    @DisplayName("Deve permitir alterar o ID e o Inventário")
    void setters_success_shouldUpdateFields() {
        var entity = new InventoryEntryEntity(id, inventory, BigDecimal.ONE, null, entryDate);
        UUID newId = UUID.randomUUID();

        entity.setId(newId);
        assertEquals(newId, entity.getId());

        assertThrows(IllegalArgumentException.class, () -> entity.setId(null));
        assertThrows(IllegalArgumentException.class, () -> entity.setInventory(null));
    }
}
