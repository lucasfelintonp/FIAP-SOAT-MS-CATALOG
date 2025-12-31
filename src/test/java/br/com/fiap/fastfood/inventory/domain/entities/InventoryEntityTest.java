package br.com.fiap.fastfood.inventory.domain.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class InventoryEntityTest {

    private UnitEntity unit;
    private UUID id;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        unit = new UnitEntity(1, "Quilograma", "KG");
        now = LocalDateTime.now();
    }

    @Test
    void constructor_success_shouldCreateEntityWithFormattedValues() {
        var entity = new InventoryEntity(
            id,
            " Carne Moída ",
            unit,
            new BigDecimal("10.555"),
            new BigDecimal("2.0"),
            " Notas de teste ",
            now,
            now
        );

        assertEquals(id, entity.getId());
        assertEquals("Carne Moída", entity.getName());
        assertEquals(new BigDecimal("10.56"), entity.getQuantity());
        assertEquals(new BigDecimal("2.00"), entity.getMinimumQuantity());
        assertEquals("Notas de teste", entity.getNotes());
        assertEquals(now, entity.getCreatedAt());
    }

    @Test
    void constructor_success_shouldGenerateIdAndDatesWhenNull() {
        var entity = new InventoryEntity(
            null,
            "Alface",
            unit,
            BigDecimal.TEN,
            BigDecimal.ONE,
            null,
            null,
            null
        );

        assertNotNull(entity.getId());
        assertNotNull(entity.getCreatedAt());
        assertNotNull(entity.getUpdatedAt());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "Esta é uma string muito longa que excede o limite de cinquenta caracteres permitidos"})
    void constructor_error_invalidName_shouldThrowException(String name) {
        assertThrows(IllegalArgumentException.class, () ->
            new InventoryEntity(id, name, unit, BigDecimal.TEN, BigDecimal.ONE, null, null, null)
        );
    }

    @Test
    void constructor_error_nullUnit_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () ->
            new InventoryEntity(id, "Item", null, BigDecimal.TEN, BigDecimal.ONE, null, null, null)
        );
    }

    @Test
    void constructor_error_invalidQuantityPrecision_shouldThrowException() {
        BigDecimal invalidQty = new BigDecimal("1234.567");
        assertThrows(IllegalArgumentException.class, () ->
            new InventoryEntity(id, "Item", unit, invalidQty, BigDecimal.ONE, null, null, null)
        );
    }

    @Test
    void constructor_error_nullMinimumQuantity_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () ->
            new InventoryEntity(id, "Item", unit, BigDecimal.TEN, null, null, null, null)
        );
    }

    @Test
    void constructor_error_tooLongNotes_shouldThrowException() {
        String longNotes = "a".repeat(101);
        assertThrows(IllegalArgumentException.class, () ->
            new InventoryEntity(id, "Item", unit, BigDecimal.TEN, BigDecimal.ONE, longNotes, null, null)
        );
    }

    @Test
    void setQuantity_success_shouldUpdateQuantityAndTimestamp() {
        var entity = new InventoryEntity(id, "Item", unit, BigDecimal.TEN, BigDecimal.ONE, null, now, now);

        LocalDateTime beforeUpdate = entity.getUpdatedAt();
        BigDecimal newQty = new BigDecimal("15.75");

        entity.setQuantity(newQty);

        assertEquals(new BigDecimal("15.75"), entity.getQuantity());
        assertTrue(entity.getUpdatedAt().isAfter(beforeUpdate) || entity.getUpdatedAt().equals(beforeUpdate));
    }

    @Test
    void setUpdatedAt_error_nullValue_shouldThrowException() {
        var entity = new InventoryEntity(id, "Item", unit, BigDecimal.TEN, BigDecimal.ONE, null, now, now);
        assertThrows(IllegalArgumentException.class, () -> entity.setUpdatedAt(null));
    }
}
