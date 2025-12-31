package br.com.fiap.fastfood.inventory.infrastructure.database.entities;

import jakarta.persistence.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InventoryEntryEntityJPATest {

    private UUID entryId;
    private InventoryEntityJPA inventory;
    private LocalDate today;

    @BeforeEach
    void setUp() {
        entryId = UUID.randomUUID();
        today = LocalDate.now();

        inventory = new InventoryEntityJPA();
        inventory.setId(UUID.randomUUID());
        inventory.setName("Pão de Hambúrguer");
    }

    @Test
    void allArgsConstructor_success_shouldCreateEntry() {
        var expiration = today.plusMonths(6);
        var entity = new InventoryEntryEntityJPA(
            entryId,
            inventory,
            new BigDecimal("50.00"),
            expiration,
            today
        );

        assertEquals(entryId, entity.getId());
        assertEquals(inventory, entity.getInventory());
        assertEquals(new BigDecimal("50.00"), entity.getQuantity());
        assertEquals(expiration, entity.getExpirationDate());
        assertEquals(today, entity.getEntryDate());
    }

    @Test
    void noArgsConstructor_success_shouldCreateEmptyEntity() {
        var entity = new InventoryEntryEntityJPA();

        assertNull(entity.getId());
        assertNull(entity.getInventory());
        assertNull(entity.getQuantity());
        assertNull(entity.getExpirationDate());
        assertNull(entity.getEntryDate());
    }

    @Test
    void setters_success_shouldSetAllFields() {
        var entity = new InventoryEntryEntityJPA();
        BigDecimal qty = new BigDecimal("100.50");

        entity.setId(entryId);
        entity.setInventory(inventory);
        entity.setQuantity(qty);
        entity.setExpirationDate(today.plusDays(30));
        entity.setEntryDate(today);

        assertEquals(entryId, entity.getId());
        assertEquals(inventory, entity.getInventory());
        assertEquals(qty, entity.getQuantity());
        assertNotNull(entity.getExpirationDate());
        assertEquals(today, entity.getEntryDate());
    }

    @Test
    void equals_success_shouldBeEqualForSameData() {
        var entity1 = new InventoryEntryEntityJPA(entryId, inventory, BigDecimal.TEN, today, today);
        var entity2 = new InventoryEntryEntityJPA(entryId, inventory, BigDecimal.TEN, today, today);

        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());
    }

    @Test
    void jpaAnnotations_success_shouldHaveCorrectTableMapping() {
        var tableAnnotation = InventoryEntryEntityJPA.class.getAnnotation(Table.class);
        assertNotNull(tableAnnotation);
        assertEquals("inventory_entry", tableAnnotation.name());
    }

    @Test
    void jpaAnnotations_success_idFieldShouldHaveCorrectAnnotations() throws NoSuchFieldException {
        var idField = InventoryEntryEntityJPA.class.getDeclaredField("id");
        assertTrue(idField.isAnnotationPresent(Id.class));

        var gen = idField.getAnnotation(GeneratedValue.class);
        assertEquals("UUID", gen.generator());
    }

    @Test
    void jpaAnnotations_success_inventoryFieldShouldHaveManyToOneMapping() throws NoSuchFieldException {
        var inventoryField = InventoryEntryEntityJPA.class.getDeclaredField("inventory");
        var manyToOne = inventoryField.getAnnotation(ManyToOne.class);
        var joinColumn = inventoryField.getAnnotation(JoinColumn.class);

        assertNotNull(manyToOne);
        assertFalse(manyToOne.optional(), "Relacionamento com Inventory deve ser obrigatório");
        assertEquals("inventory_id", joinColumn.name());
        assertFalse(joinColumn.nullable());
    }

    @Test
    void jpaAnnotations_success_quantityFieldShouldHavePrecision() throws NoSuchFieldException {
        var quantityField = InventoryEntryEntityJPA.class.getDeclaredField("quantity");
        var column = quantityField.getAnnotation(Column.class);

        assertNotNull(column);
        assertEquals(5, column.precision());
        assertEquals(2, column.scale());
    }

    @Test
    void toString_success_shouldContainMainInfo() {
        var entity = new InventoryEntryEntityJPA();
        entity.setQuantity(new BigDecimal("25.00"));

        String toString = entity.toString();
        assertTrue(toString.contains("25.00"));
    }
}
