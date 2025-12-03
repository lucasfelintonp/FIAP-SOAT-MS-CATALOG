package br.com.fiap.fastfood.product.infrastructure.database.entities;

import jakarta.persistence.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductEntityJPATest {

    private UUID coxinhaId;
    private UUID refrigeranteId;
    private UUID hamburguerId;
    private LocalDateTime fixedDateTime;

    @BeforeEach
    void setUp() {
        coxinhaId = UUID.randomUUID();
        refrigeranteId = UUID.randomUUID();
        hamburguerId = UUID.randomUUID();
        fixedDateTime = LocalDateTime.of(2025, 12, 2, 10, 30, 0);
    }

    // ==================== CONSTRUCTOR TESTS ====================

    @Test
    void allArgsConstructor_success_shouldCreateCoxinha() {
        var entity = new ProductEntityJPA(
            coxinhaId,
            "Coxinha",
            "Coxinha de frango frita crocante",
            new BigDecimal("6.50"),
            true,
            "/images/coxinha.jpg",
            1,
            fixedDateTime,
            fixedDateTime,
            null
        );

        assertEquals(coxinhaId, entity.getId());
        assertEquals("Coxinha", entity.getName());
        assertEquals("Coxinha de frango frita crocante", entity.getDescription());
        assertEquals(new BigDecimal("6.50"), entity.getPrice());
        assertTrue(entity.getIsActive());
        assertEquals("/images/coxinha.jpg", entity.getImagePath());
        assertEquals(1, entity.getCategoryId());
        assertEquals(fixedDateTime, entity.getCreatedAt());
        assertEquals(fixedDateTime, entity.getUpdatedAt());
        assertNull(entity.getDeletedAt());
    }

    @Test
    void allArgsConstructor_success_shouldCreateRefrigerante() {
        var entity = new ProductEntityJPA(
            refrigeranteId,
            "Refrigerante Coca-Cola",
            "Refrigerante Coca-Cola 350ml gelado",
            new BigDecimal("5.00"),
            true,
            "/images/coca-cola.jpg",
            2,
            fixedDateTime,
            fixedDateTime,
            null
        );

        assertEquals(refrigeranteId, entity.getId());
        assertEquals("Refrigerante Coca-Cola", entity.getName());
        assertEquals(new BigDecimal("5.00"), entity.getPrice());
        assertEquals(2, entity.getCategoryId());
    }

    @Test
    void allArgsConstructor_success_shouldCreateXBurger() {
        var entity = new ProductEntityJPA(
            hamburguerId,
            "X-Burger",
            "Hambúrguer artesanal 180g com queijo cheddar",
            new BigDecimal("18.90"),
            true,
            "/images/xburger.jpg",
            1,
            fixedDateTime,
            fixedDateTime,
            null
        );

        assertEquals(hamburguerId, entity.getId());
        assertEquals("X-Burger", entity.getName());
        assertEquals("Hambúrguer artesanal 180g com queijo cheddar", entity.getDescription());
        assertEquals(new BigDecimal("18.90"), entity.getPrice());
    }

    @Test
    void allArgsConstructor_success_shouldCreateInactiveProduct() {
        var entity = new ProductEntityJPA(
            UUID.randomUUID(),
            "Produto Descontinuado",
            "Produto fora de linha",
            new BigDecimal("0.00"),
            false,  // Produto inativo
            null,
            1,
            fixedDateTime,
            fixedDateTime,
            null
        );

        assertFalse(entity.getIsActive());
        assertEquals("Produto Descontinuado", entity.getName());
    }

    @Test
    void allArgsConstructor_success_shouldCreateDeletedProduct() {
        LocalDateTime deletedTime = fixedDateTime.plusDays(7);

        var entity = new ProductEntityJPA(
            coxinhaId,
            "Coxinha",
            "Coxinha de frango",
            new BigDecimal("6.50"),
            true,
            "/images/coxinha.jpg",
            1,
            fixedDateTime,
            fixedDateTime,
            deletedTime  // Produto deletado
        );

        assertEquals(deletedTime, entity.getDeletedAt());
    }

    @Test
    void noArgsConstructor_success_shouldCreateEmptyEntity() {
        var entity = new ProductEntityJPA();

        assertNull(entity.getId());
        assertNull(entity.getName());
        assertNull(entity.getDescription());
        assertNull(entity.getPrice());
        assertNull(entity.getIsActive());
        assertNull(entity.getImagePath());
        assertNull(entity.getCategoryId());
        assertNull(entity.getCreatedAt());
        assertNull(entity.getUpdatedAt());
        assertNull(entity.getDeletedAt());
    }

    // ==================== SETTER TESTS ====================

    @Test
    void setters_success_shouldSetAllFieldsForBatataFrita() {
        var entity = new ProductEntityJPA();
        var batataId = UUID.randomUUID();

        entity.setId(batataId);
        entity.setName("Batata Frita");
        entity.setDescription("Porção de batata frita crocante 300g");
        entity.setPrice(new BigDecimal("12.00"));
        entity.setIsActive(true);
        entity.setImagePath("/images/batata.jpg");
        entity.setCategoryId(3);
        entity.setCreatedAt(fixedDateTime);
        entity.setUpdatedAt(fixedDateTime);
        entity.setDeletedAt(null);

        assertEquals(batataId, entity.getId());
        assertEquals("Batata Frita", entity.getName());
        assertEquals("Porção de batata frita crocante 300g", entity.getDescription());
        assertEquals(new BigDecimal("12.00"), entity.getPrice());
        assertTrue(entity.getIsActive());
        assertEquals("/images/batata.jpg", entity.getImagePath());
        assertEquals(3, entity.getCategoryId());
        assertEquals(fixedDateTime, entity.getCreatedAt());
        assertEquals(fixedDateTime, entity.getUpdatedAt());
        assertNull(entity.getDeletedAt());
    }

    @Test
    void setters_success_shouldSetAllFieldsForSorvete() {
        var entity = new ProductEntityJPA();

        entity.setName("Sorvete de Chocolate");
        entity.setDescription("Sorvete cremoso sabor chocolate belga");
        entity.setPrice(new BigDecimal("8.50"));
        entity.setIsActive(true);
        entity.setImagePath("/images/sorvete-chocolate.jpg");
        entity.setCategoryId(4);

        assertEquals("Sorvete de Chocolate", entity.getName());
        assertEquals(new BigDecimal("8.50"), entity.getPrice());
        assertEquals(4, entity.getCategoryId());
    }

    @Test
    void setters_success_shouldUpdatePriceFromCoxinha() {
        var entity = new ProductEntityJPA(
            coxinhaId,
            "Coxinha",
            "Coxinha de frango",
            new BigDecimal("6.50"),
            true,
            "/images/coxinha.jpg",
            1,
            fixedDateTime,
            fixedDateTime,
            null
        );

        // Atualizar preço
        entity.setPrice(new BigDecimal("7.00"));

        assertEquals(new BigDecimal("7.00"), entity.getPrice());
        assertEquals("Coxinha", entity.getName());
    }

    @Test
    void setters_success_shouldDeactivateProduct() {
        var entity = new ProductEntityJPA(
            refrigeranteId,
            "Refrigerante",
            "Bebida gelada",
            new BigDecimal("5.00"),
            true,
            "/images/bebida.jpg",
            2,
            fixedDateTime,
            fixedDateTime,
            null
        );

        // Desativar produto
        entity.setIsActive(false);

        assertFalse(entity.getIsActive());
    }

    @Test
    void setters_success_shouldMarkProductAsDeleted() {
        var entity = new ProductEntityJPA(
            coxinhaId,
            "Coxinha",
            "Coxinha de frango",
            new BigDecimal("6.50"),
            true,
            "/images/coxinha.jpg",
            1,
            fixedDateTime,
            fixedDateTime,
            null
        );

        LocalDateTime deletionTime = LocalDateTime.now();
        entity.setDeletedAt(deletionTime);

        assertEquals(deletionTime, entity.getDeletedAt());
        assertNotNull(entity.getDeletedAt());
    }

    // ==================== LIFECYCLE CALLBACKS TESTS ====================

    @Test
    void onPrePersist_success_shouldSetCreatedAtAndUpdatedAt() {
        var entity = new ProductEntityJPA();
        entity.setName("Pastel");
        entity.setDescription("Pastel de carne");
        entity.setPrice(new BigDecimal("5.50"));
        entity.setIsActive(true);
        entity.setCategoryId(1);

        LocalDateTime before = LocalDateTime.now().minusSeconds(1);
        entity.onPrePersist();
        LocalDateTime after = LocalDateTime.now().plusSeconds(1);

        assertNotNull(entity.getCreatedAt());
        assertNotNull(entity.getUpdatedAt());
        assertTrue(entity.getCreatedAt().isAfter(before) && entity.getCreatedAt().isBefore(after));
        assertTrue(entity.getUpdatedAt().isAfter(before) && entity.getUpdatedAt().isBefore(after));
        assertEquals(entity.getCreatedAt(), entity.getUpdatedAt());
    }

    @Test
    void onPrePersist_success_shouldSetSameTimeForCreatedAndUpdated() {
        var entity = new ProductEntityJPA();
        entity.setName("Nuggets");
        entity.setDescription("6 unidades de nuggets de frango");
        entity.setPrice(new BigDecimal("10.00"));

        entity.onPrePersist();

        assertNotNull(entity.getCreatedAt());
        assertNotNull(entity.getUpdatedAt());
        assertEquals(entity.getCreatedAt(), entity.getUpdatedAt());
    }

    @Test
    void onPreUpdate_success_shouldUpdateOnlyUpdatedAt() {
        var entity = new ProductEntityJPA();
        entity.setName("Milkshake");
        entity.setDescription("Milkshake de morango 400ml");
        entity.setPrice(new BigDecimal("12.50"));

        // Simula criação
        entity.onPrePersist();
        LocalDateTime originalCreatedAt = entity.getCreatedAt();
        LocalDateTime originalUpdatedAt = entity.getUpdatedAt();

        // Simula atualização - aguarda garantia de tempo diferente
        entity.setPrice(new BigDecimal("13.00"));
        entity.onPreUpdate();

        assertEquals(originalCreatedAt, entity.getCreatedAt(), "CreatedAt não deve ser alterado no update");
        assertNotEquals(originalUpdatedAt, entity.getUpdatedAt(), "UpdatedAt deve ser alterado");
        // UpdatedAt deve ser igual ou posterior ao originalUpdatedAt
        assertFalse(entity.getUpdatedAt().isBefore(originalUpdatedAt));
    }

    @Test
    void onPreUpdate_success_shouldSetUpdatedAtToCurrentTime() {
        var entity = new ProductEntityJPA();
        entity.setCreatedAt(fixedDateTime);
        entity.setUpdatedAt(fixedDateTime);
        entity.setName("Açaí");
        entity.setPrice(new BigDecimal("15.00"));

        LocalDateTime before = LocalDateTime.now().minusSeconds(1);
        entity.onPreUpdate();
        LocalDateTime after = LocalDateTime.now().plusSeconds(1);

        assertEquals(fixedDateTime, entity.getCreatedAt());
        assertTrue(entity.getUpdatedAt().isAfter(before) && entity.getUpdatedAt().isBefore(after));
        assertTrue(entity.getUpdatedAt().isAfter(entity.getCreatedAt()));
    }

    // ==================== EQUALS AND HASHCODE TESTS ====================

    @Test
    void equals_success_shouldHaveConsistentHashCode() {
        var entity = new ProductEntityJPA(
            coxinhaId,
            "Coxinha",
            "Coxinha de frango",
            new BigDecimal("6.50"),
            true,
            "/images/coxinha.jpg",
            1,
            fixedDateTime,
            fixedDateTime,
            null
        );

        // Verifica que hashCode é consistente para o mesmo objeto
        int hashCode1 = entity.hashCode();
        int hashCode2 = entity.hashCode();
        assertEquals(hashCode1, hashCode2, "HashCode deve ser consistente");
    }

    @Test
    void equals_success_shouldReturnTrueForEqualProducts() {
        var entity1 = new ProductEntityJPA(
            coxinhaId,
            "Coxinha",
            "Coxinha de frango",
            new BigDecimal("6.50"),
            true,
            "/images/coxinha.jpg",
            1,
            fixedDateTime,
            fixedDateTime,
            null
        );

        var entity2 = new ProductEntityJPA(
            coxinhaId,
            "Coxinha",
            "Coxinha de frango",
            new BigDecimal("6.50"),
            true,
            "/images/coxinha.jpg",
            1,
            fixedDateTime,
            fixedDateTime,
            null
        );

        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());
    }

    @Test
    void equals_error_shouldReturnFalseForDifferentIds() {
        var coxinha = new ProductEntityJPA(
            coxinhaId,
            "Coxinha",
            "Coxinha de frango",
            new BigDecimal("6.50"),
            true,
            "/images/coxinha.jpg",
            1,
            fixedDateTime,
            fixedDateTime,
            null
        );

        var refrigerante = new ProductEntityJPA(
            refrigeranteId,
            "Coxinha",
            "Coxinha de frango",
            new BigDecimal("6.50"),
            true,
            "/images/coxinha.jpg",
            1,
            fixedDateTime,
            fixedDateTime,
            null
        );

        assertNotEquals(coxinha, refrigerante);
    }

    @Test
    void equals_error_shouldReturnFalseForNull() {
        var entity = new ProductEntityJPA(
            coxinhaId,
            "Coxinha",
            "Coxinha de frango",
            new BigDecimal("6.50"),
            true,
            "/images/coxinha.jpg",
            1,
            fixedDateTime,
            fixedDateTime,
            null
        );

        assertNotEquals(null, entity);
    }

    @Test
    void equals_error_shouldReturnFalseForDifferentClass() {
        var entity = new ProductEntityJPA(
            coxinhaId,
            "Coxinha",
            "Coxinha de frango",
            new BigDecimal("6.50"),
            true,
            "/images/coxinha.jpg",
            1,
            fixedDateTime,
            fixedDateTime,
            null
        );

        assertNotEquals("Not a ProductEntityJPA", entity);
        assertNotEquals(123, entity);
    }

    // ==================== JPA ANNOTATIONS TESTS ====================

    @Test
    void jpaAnnotations_success_shouldHaveCorrectEntityAnnotation() {
        var entityAnnotation = ProductEntityJPA.class.getAnnotation(Entity.class);

        assertNotNull(entityAnnotation);
        assertEquals("product", entityAnnotation.name());
    }

    @Test
    void jpaAnnotations_success_shouldHaveCorrectTableAnnotation() {
        var tableAnnotation = ProductEntityJPA.class.getAnnotation(Table.class);

        assertNotNull(tableAnnotation);
        assertEquals("products", tableAnnotation.name());
    }

    @Test
    void jpaAnnotations_success_idFieldShouldHaveCorrectAnnotations() throws NoSuchFieldException {
        var idField = ProductEntityJPA.class.getDeclaredField("id");

        assertTrue(idField.isAnnotationPresent(Id.class), "id deve ter @Id");
        assertTrue(idField.isAnnotationPresent(GeneratedValue.class), "id deve ter @GeneratedValue");

        var columnAnnotation = idField.getAnnotation(Column.class);
        assertNotNull(columnAnnotation);
        assertEquals("id", columnAnnotation.name());
        assertFalse(columnAnnotation.updatable());
        assertFalse(columnAnnotation.nullable());
    }

    @Test
    void jpaAnnotations_success_nameFieldShouldHaveCorrectConstraints() throws NoSuchFieldException {
        var nameField = ProductEntityJPA.class.getDeclaredField("name");
        var columnAnnotation = nameField.getAnnotation(Column.class);

        assertNotNull(columnAnnotation);
        assertEquals("name", columnAnnotation.name());
        assertEquals(100, columnAnnotation.length());
        assertFalse(columnAnnotation.nullable());
    }

    @Test
    void jpaAnnotations_success_descriptionFieldShouldBeText() throws NoSuchFieldException {
        var descField = ProductEntityJPA.class.getDeclaredField("description");
        var columnAnnotation = descField.getAnnotation(Column.class);

        assertNotNull(columnAnnotation);
        assertEquals("description", columnAnnotation.name());
        assertEquals("text", columnAnnotation.columnDefinition());
        assertFalse(columnAnnotation.nullable());
    }

    @Test
    void jpaAnnotations_success_priceFieldShouldHaveCorrectPrecisionAndScale() throws NoSuchFieldException {
        var priceField = ProductEntityJPA.class.getDeclaredField("price");
        var columnAnnotation = priceField.getAnnotation(Column.class);

        assertNotNull(columnAnnotation);
        assertEquals("price", columnAnnotation.name());
        assertEquals(5, columnAnnotation.precision());
        assertEquals(2, columnAnnotation.scale());
        assertFalse(columnAnnotation.nullable());
    }

    @Test
    void jpaAnnotations_success_isActiveFieldShouldBeNotNullable() throws NoSuchFieldException {
        var isActiveField = ProductEntityJPA.class.getDeclaredField("isActive");
        var columnAnnotation = isActiveField.getAnnotation(Column.class);

        assertNotNull(columnAnnotation);
        assertEquals("is_active", columnAnnotation.name());
        assertFalse(columnAnnotation.nullable());
    }

    @Test
    void jpaAnnotations_success_imagePathFieldShouldHaveMaxLength() throws NoSuchFieldException {
        var imagePathField = ProductEntityJPA.class.getDeclaredField("imagePath");
        var columnAnnotation = imagePathField.getAnnotation(Column.class);

        assertNotNull(columnAnnotation);
        assertEquals("image_path", columnAnnotation.name());
        assertEquals(250, columnAnnotation.length());
        assertTrue(columnAnnotation.nullable(), "imagePath pode ser nulo");
    }

    @Test
    void jpaAnnotations_success_categoryIdFieldShouldBeNotNullable() throws NoSuchFieldException {
        var categoryIdField = ProductEntityJPA.class.getDeclaredField("categoryId");
        var columnAnnotation = categoryIdField.getAnnotation(Column.class);

        assertNotNull(columnAnnotation);
        assertEquals("category_id", columnAnnotation.name());
        assertFalse(columnAnnotation.nullable());
    }

    @Test
    void jpaAnnotations_success_createdAtFieldShouldBeNotNullable() throws NoSuchFieldException {
        var createdAtField = ProductEntityJPA.class.getDeclaredField("createdAt");
        var columnAnnotation = createdAtField.getAnnotation(Column.class);

        assertNotNull(columnAnnotation);
        assertEquals("created_at", columnAnnotation.name());
        assertFalse(columnAnnotation.nullable());
    }

    @Test
    void jpaAnnotations_success_updatedAtFieldShouldBeNotNullable() throws NoSuchFieldException {
        var updatedAtField = ProductEntityJPA.class.getDeclaredField("updatedAt");
        var columnAnnotation = updatedAtField.getAnnotation(Column.class);

        assertNotNull(columnAnnotation);
        assertEquals("updated_at", columnAnnotation.name());
        assertFalse(columnAnnotation.nullable());
    }

    @Test
    void jpaAnnotations_success_deletedAtFieldShouldBeNullable() throws NoSuchFieldException {
        var deletedAtField = ProductEntityJPA.class.getDeclaredField("deletedAt");
        var columnAnnotation = deletedAtField.getAnnotation(Column.class);

        assertNotNull(columnAnnotation);
        assertEquals("deleted_at", columnAnnotation.name());
        assertTrue(columnAnnotation.nullable(), "deletedAt deve ser nullable para soft delete");
    }

    @Test
    void jpaAnnotations_success_shouldHavePrePersistMethod() throws NoSuchMethodException {
        var prePersistMethod = ProductEntityJPA.class.getDeclaredMethod("onPrePersist");

        assertNotNull(prePersistMethod);
        assertTrue(prePersistMethod.isAnnotationPresent(PrePersist.class));
    }

    @Test
    void jpaAnnotations_success_shouldHavePreUpdateMethod() throws NoSuchMethodException {
        var preUpdateMethod = ProductEntityJPA.class.getDeclaredMethod("onPreUpdate");

        assertNotNull(preUpdateMethod);
        assertTrue(preUpdateMethod.isAnnotationPresent(PreUpdate.class));
    }

    // ==================== BUSINESS LOGIC TESTS ====================

    @Test
    void businessLogic_success_shouldAllowNullImagePath() {
        var entity = new ProductEntityJPA(
            UUID.randomUUID(),
            "Água Mineral",
            "Água mineral sem gás 500ml",
            new BigDecimal("3.00"),
            true,
            null,  // Sem imagem
            2,
            fixedDateTime,
            fixedDateTime,
            null
        );

        assertNull(entity.getImagePath());
        assertEquals("Água Mineral", entity.getName());
    }

    @Test
    void businessLogic_success_shouldSupportLongDescriptions() {
        String longDescription = "Hambúrguer artesanal com pão australiano, " +
                "carne 180g, queijo cheddar derretido, alface americana, " +
                "tomate frescos, cebola roxa, molho especial da casa e " +
                "batata palha crocante. Acompanha fritas e molho.";

        var entity = new ProductEntityJPA(
            hamburguerId,
            "X-Burger Premium",
            longDescription,
            new BigDecimal("25.90"),
            true,
            "/images/xburger-premium.jpg",
            1,
            fixedDateTime,
            fixedDateTime,
            null
        );

        assertEquals(longDescription, entity.getDescription());
        assertTrue(entity.getDescription().length() > 100);
    }

    @Test
    void businessLogic_success_shouldSupportDecimalPrices() {
        var prices = new BigDecimal[] {
            new BigDecimal("6.50"),   // Coxinha
            new BigDecimal("5.00"),   // Refrigerante
            new BigDecimal("18.90"),  // X-Burger
            new BigDecimal("12.00"),  // Batata Frita
            new BigDecimal("8.50"),   // Sorvete
            new BigDecimal("3.99"),   // Promoção
            new BigDecimal("999.99")  // Preço máximo (5 dígitos, 2 decimais)
        };

        for (BigDecimal price : prices) {
            var entity = new ProductEntityJPA();
            entity.setPrice(price);
            assertEquals(price, entity.getPrice());
        }
    }

    @Test
    void businessLogic_success_shouldSupportAllCategories() {
        // 1 = Lanches, 2 = Bebidas, 3 = Acompanhamentos, 4 = Sobremesas
        var categories = new Integer[] {1, 2, 3, 4};

        for (Integer categoryId : categories) {
            var entity = new ProductEntityJPA();
            entity.setCategoryId(categoryId);
            assertEquals(categoryId, entity.getCategoryId());
        }
    }

    @Test
    void businessLogic_success_shouldTrackProductLifecycle() {
        var entity = new ProductEntityJPA();
        entity.setName("Pizza");
        entity.setDescription("Pizza margherita");
        entity.setPrice(new BigDecimal("35.00"));
        entity.setIsActive(true);
        entity.setCategoryId(1);

        // Criação
        entity.onPrePersist();
        LocalDateTime created = entity.getCreatedAt();
        assertNotNull(created);
        assertTrue(entity.getIsActive());
        assertNull(entity.getDeletedAt());

        // Atualização
        entity.onPreUpdate();
        assertFalse(entity.getUpdatedAt().isBefore(created));

        // Desativação
        entity.setIsActive(false);
        assertFalse(entity.getIsActive());

        // Soft Delete
        entity.setDeletedAt(LocalDateTime.now());
        assertNotNull(entity.getDeletedAt());
    }

    @Test
    void toString_success_shouldContainMainProductInfo() {
        var entity = new ProductEntityJPA(
            coxinhaId,
            "Coxinha",
            "Coxinha de frango",
            new BigDecimal("6.50"),
            true,
            "/images/coxinha.jpg",
            1,
            fixedDateTime,
            fixedDateTime,
            null
        );

        String toString = entity.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("Coxinha"));
        assertTrue(toString.contains("6.50"));
        assertTrue(toString.contains(coxinhaId.toString()));
    }
}

