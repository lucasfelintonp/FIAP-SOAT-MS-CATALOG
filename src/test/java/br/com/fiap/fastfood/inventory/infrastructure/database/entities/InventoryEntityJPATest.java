package br.com.fiap.fastfood.inventory.infrastructure.database.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InventoryEntityJPATest {

    private UUID inventoryId;
    private UnitEntityJPA unitEntity;
    private LocalDateTime fixedDateTime;

    @BeforeEach
    void setUp() {
        inventoryId = UUID.randomUUID();
        fixedDateTime = LocalDateTime.of(2025, 12, 31, 9, 0, 0);

        unitEntity = new UnitEntityJPA();
        unitEntity.setId(1);
        unitEntity.setName("Quilograma");
        unitEntity.setAbbreviation("KG");
    }

    @Test
    void allArgsConstructor_success_shouldCreateInventoryItem() {
        var entity = new InventoryEntityJPA(
            inventoryId,
            "Carne Moída",
            unitEntity,
            new BigDecimal("10.00"),
            new BigDecimal("2.00"),
            "Lote A-1",
            fixedDateTime,
            fixedDateTime
        );

        assertEquals(inventoryId, entity.getId());
        assertEquals("Carne Moída", entity.getName());
        assertEquals(unitEntity, entity.getUnit());
        assertEquals(new BigDecimal("10.00"), entity.getQuantity());
        assertEquals(new BigDecimal("2.00"), entity.getMinimumQuantity());
        assertEquals("Lote A-1", entity.getNotes());
        assertEquals(fixedDateTime, entity.getCreatedAt());
        assertEquals(fixedDateTime, entity.getUpdatedAt());
    }

    @Test
    void noArgsConstructor_success_shouldCreateEmptyEntity() {
        var entity = new InventoryEntityJPA();

        assertNull(entity.getId());
        assertNull(entity.getName());
        assertNull(entity.getUnit());
        assertNull(entity.getQuantity());
        assertNull(entity.getMinimumQuantity());
        assertNull(entity.getNotes());
        assertNull(entity.getCreatedAt());
        assertNull(entity.getUpdatedAt());
    }

    @Test
    void setters_success_shouldSetAllFields() {
        var entity = new InventoryEntityJPA();

        entity.setId(inventoryId);
        entity.setName("Tomate");
        entity.setUnit(unitEntity);
        entity.setQuantity(new BigDecimal("50.00"));
        entity.setMinimumQuantity(new BigDecimal("5.00"));
        entity.setNotes("Tomate cereja");
        entity.setCreatedAt(fixedDateTime);
        entity.setUpdatedAt(fixedDateTime);

        assertEquals(inventoryId, entity.getId());
        assertEquals("Tomate", entity.getName());
        assertEquals(new BigDecimal("50.00"), entity.getQuantity());
        assertEquals(unitEntity, entity.getUnit());
        assertEquals("Tomate cereja", entity.getNotes());
    }

    @Test
    void equals_success_shouldReturnTrueForEqualEntities() {
        var entity1 = new InventoryEntityJPA(inventoryId, "Item", unitEntity, BigDecimal.ONE, BigDecimal.ONE, "Nota", fixedDateTime, fixedDateTime);
        var entity2 = new InventoryEntityJPA(inventoryId, "Item", unitEntity, BigDecimal.ONE, BigDecimal.ONE, "Nota", fixedDateTime, fixedDateTime);

        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());
    }

    @Test
    void equals_error_shouldReturnFalseForDifferentIds() {
        var entity1 = new InventoryEntityJPA(UUID.randomUUID(), "Item", unitEntity, BigDecimal.ONE, BigDecimal.ONE, "Nota", fixedDateTime, fixedDateTime);
        var entity2 = new InventoryEntityJPA(UUID.randomUUID(), "Item", unitEntity, BigDecimal.ONE, BigDecimal.ONE, "Nota", fixedDateTime, fixedDateTime);

        assertNotEquals(entity1, entity2);
    }

    @Test
    void jpaAnnotations_success_shouldHaveCorrectTableMapping() {
        var tableAnnotation = InventoryEntityJPA.class.getAnnotation(Table.class);
        assertNotNull(tableAnnotation);
        assertEquals("inventory", tableAnnotation.name());
    }

    @Test
    void jpaAnnotations_success_idFieldShouldHaveCorrectAnnotations() throws NoSuchFieldException {
        var idField = InventoryEntityJPA.class.getDeclaredField("id");
        assertTrue(idField.isAnnotationPresent(Id.class));
        assertTrue(idField.isAnnotationPresent(GeneratedValue.class));

        var gen = idField.getAnnotation(GeneratedValue.class);
        assertEquals("UUID", gen.generator());
    }

    @Test
    void jpaAnnotations_success_nameFieldShouldHaveCorrectConstraints() throws NoSuchFieldException {
        var nameField = InventoryEntityJPA.class.getDeclaredField("name");
        var column = nameField.getAnnotation(Column.class);

        assertFalse(column.nullable());
        assertEquals(50, column.length());
    }

    @Test
    void jpaAnnotations_success_unitFieldShouldHaveManyToOneMapping() throws NoSuchFieldException {
        var unitField = InventoryEntityJPA.class.getDeclaredField("unit");
        var manyToOne = unitField.getAnnotation(ManyToOne.class);
        var joinColumn = unitField.getAnnotation(JoinColumn.class);

        assertNotNull(manyToOne);
        assertFalse(manyToOne.optional());
        assertEquals("unit_id", joinColumn.name());
    }

    @Test
    void jpaAnnotations_success_quantityFieldsShouldHaveCorrectPrecision() throws NoSuchFieldException {
        var quantityField = InventoryEntityJPA.class.getDeclaredField("quantity");
        var column = quantityField.getAnnotation(Column.class);

        assertEquals(5, column.precision());
        assertEquals(2, column.scale());
    }

    @Test
    void jpaAnnotations_success_auditFieldsShouldHaveHibernateAnnotations() throws NoSuchFieldException {
        var createdAtField = InventoryEntityJPA.class.getDeclaredField("createdAt");
        var updatedAtField = InventoryEntityJPA.class.getDeclaredField("updatedAt");

        assertTrue(createdAtField.isAnnotationPresent(CreationTimestamp.class));
        assertTrue(updatedAtField.isAnnotationPresent(UpdateTimestamp.class));

        var createdColumn = createdAtField.getAnnotation(Column.class);
        assertFalse(createdColumn.updatable());
        assertFalse(createdColumn.nullable());
    }

    @Test
    void toString_success_shouldContainInventoryName() {
        var entity = new InventoryEntityJPA();
        entity.setName("Estoque Central");

        String toString = entity.toString();
        assertTrue(toString.contains("Estoque Central"));
    }

    @Test
    void businessLogic_success_shouldAllowLargeQuantities() {
        var entity = new InventoryEntityJPA();
        BigDecimal largeQty = new BigDecimal("999.99");
        entity.setQuantity(largeQty);

        assertEquals(0, largeQty.compareTo(entity.getQuantity()));
    }
}
