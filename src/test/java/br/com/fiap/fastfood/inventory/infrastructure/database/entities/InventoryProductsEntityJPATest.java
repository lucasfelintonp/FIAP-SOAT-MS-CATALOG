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

class InventoryProductsEntityJPATest {

    private UUID relationId;
    private UUID productId;
    private InventoryEntityJPA inventory;
    private LocalDateTime fixedDateTime;

    @BeforeEach
    void setUp() {
        relationId = UUID.randomUUID();
        productId = UUID.randomUUID();
        fixedDateTime = LocalDateTime.of(2025, 12, 31, 9, 0, 0);

        inventory = new InventoryEntityJPA();
        inventory.setId(UUID.randomUUID());
        inventory.setName("Carne de Hambúrguer");
    }

    @Test
    void allArgsConstructor_success_shouldCreateRelation() {
        var entity = new InventoryProductsEntityJPA(
            relationId,
            productId,
            inventory,
            new BigDecimal("0.150"),
            fixedDateTime,
            fixedDateTime
        );

        assertEquals(relationId, entity.getId());
        assertEquals(productId, entity.getProductId());
        assertEquals(inventory, entity.getInventory());
        assertEquals(new BigDecimal("0.150"), entity.getQuantity());
        assertEquals(fixedDateTime, entity.getCreatedAt());
        assertEquals(fixedDateTime, entity.getUpdatedAt());
    }

    @Test
    void noArgsConstructor_success_shouldCreateEmptyEntity() {
        var entity = new InventoryProductsEntityJPA();

        assertNull(entity.getId());
        assertNull(entity.getProductId());
        assertNull(entity.getInventory());
        assertNull(entity.getQuantity());
        assertNull(entity.getCreatedAt());
        assertNull(entity.getUpdatedAt());
    }

    @Test
    void setters_success_shouldSetAllFields() {
        var entity = new InventoryProductsEntityJPA();
        BigDecimal qty = new BigDecimal("1.00");

        entity.setId(relationId);
        entity.setProductId(productId);
        entity.setInventory(inventory);
        entity.setQuantity(qty);
        entity.setCreatedAt(fixedDateTime);
        entity.setUpdatedAt(fixedDateTime);

        assertEquals(relationId, entity.getId());
        assertEquals(productId, entity.getProductId());
        assertEquals(inventory, entity.getInventory());
        assertEquals(qty, entity.getQuantity());
    }

    @Test
    void equals_success_shouldBeEqualForSameData() {
        var entity1 = new InventoryProductsEntityJPA(relationId, productId, inventory, BigDecimal.ONE, fixedDateTime, fixedDateTime);
        var entity2 = new InventoryProductsEntityJPA(relationId, productId, inventory, BigDecimal.ONE, fixedDateTime, fixedDateTime);

        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());
    }

    @Test
    void jpaAnnotations_success_shouldHaveCorrectTableMapping() {
        var tableAnnotation = InventoryProductsEntityJPA.class.getAnnotation(Table.class);
        assertNotNull(tableAnnotation);
        assertEquals("inventory_products", tableAnnotation.name());
    }

    @Test
    void jpaAnnotations_success_idFieldShouldHaveCorrectAnnotations() throws NoSuchFieldException {
        var idField = InventoryProductsEntityJPA.class.getDeclaredField("id");
        assertTrue(idField.isAnnotationPresent(Id.class));

        var gen = idField.getAnnotation(GeneratedValue.class);
        assertEquals("UUID", gen.generator());
    }

    @Test
    void jpaAnnotations_success_inventoryFieldShouldHaveJoinColumn() throws NoSuchFieldException {
        var inventoryField = InventoryProductsEntityJPA.class.getDeclaredField("inventory");
        assertTrue(inventoryField.isAnnotationPresent(ManyToOne.class));

        var joinColumn = inventoryField.getAnnotation(JoinColumn.class);
        assertEquals("inventory_id", joinColumn.name());
    }

    @Test
    void jpaAnnotations_success_auditFieldsShouldHaveHibernateAnnotations() throws NoSuchFieldException {
        var createdAtField = InventoryProductsEntityJPA.class.getDeclaredField("createdAt");
        var updatedAtField = InventoryProductsEntityJPA.class.getDeclaredField("updatedAt");

        assertTrue(createdAtField.isAnnotationPresent(CreationTimestamp.class));
        assertTrue(updatedAtField.isAnnotationPresent(UpdateTimestamp.class));

        var createdColumn = createdAtField.getAnnotation(Column.class);
        assertFalse(createdColumn.updatable(), "createdAt não pode ser atualizado");
        assertFalse(createdColumn.nullable(), "createdAt não pode ser nulo");
    }

    @Test
    void toString_success_shouldContainIds() {
        var entity = new InventoryProductsEntityJPA();
        entity.setProductId(productId);

        String toString = entity.toString();
        assertTrue(toString.contains(productId.toString()));
    }
}
