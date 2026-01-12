package br.com.fiap.fastfood.product.application.controllers;

import br.com.fiap.fastfood.inventory.application.dtos.CreateInventoryProductDTO;
import br.com.fiap.fastfood.inventory.infrastructure.interfaces.InventoryDatasource;
import br.com.fiap.fastfood.product.application.dtos.CreateProductDTO;
import br.com.fiap.fastfood.product.application.dtos.ProductDTO;
import br.com.fiap.fastfood.product.application.dtos.UpdateProductDTO;
import br.com.fiap.fastfood.product.infrastructure.interfaces.ProductDatasource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductDatasource datasource;

    @Mock
    private InventoryDatasource inventoryDatasource;

    private ProductController controller;

    @BeforeEach
    void setUp() {
        controller = new ProductController(datasource, inventoryDatasource);
    }

    // ==================== CREATE TESTS ====================

    @Test
    @DisplayName("Should create product successfully - Coxinha")
    void create_shouldReturnCreatedProduct_Coxinha() {
        var createDto = new CreateProductDTO(
            "Coxinha",
            "Coxinha de frango frita",
            new BigDecimal("6.50"),
            true,
            "/images/coxinha.jpg",
            1,
            null
        );

        var id = UUID.randomUUID();
        var now = LocalDateTime.now();
        var returned = new ProductDTO(id, "Coxinha", "Coxinha de frango frita", new BigDecimal("6.50"), true, "/images/coxinha.jpg", 1, now, now);

        when(datasource.create(any())).thenReturn(returned);

        var result = controller.create(createDto);

        assertNotNull(result);
        assertEquals(id, result.id());
        assertEquals("Coxinha", result.name());

        verify(datasource, times(1)).create(any());
    }

    @Test
    @DisplayName("Should propagate exception when datasource fails during creation")
    void create_shouldPropagatePersistenceError_whenDatasourceFails() {
        var createDto = new CreateProductDTO(
            "Refri Lata",
            "Refrigerante cola 350ml",
            new BigDecimal("4.00"),
            true,
            "/images/refri.jpg",
            2,
            null
        );

        when(datasource.create(any())).thenThrow(new RuntimeException("DB down"));

        assertThrows(RuntimeException.class, () -> controller.create(createDto));

        verify(datasource, times(1)).create(any());
    }

    // ==================== FIND ALL TESTS ====================

    @Test
    @DisplayName("Should return products filtered by category - Bebidas")
    void findAll_shouldReturnProductsByCategory_1_Bebidas() {
        var now = LocalDateTime.now();
        var list = List.of(
            new ProductDTO(UUID.randomUUID(), "Refrigerante Lata", "Refrigerante cola 350ml", new BigDecimal("4.00"), true, "/images/refri.jpg", 1, now, now),
            new ProductDTO(UUID.randomUUID(), "Suco Natural", "Suco de laranja 300ml", new BigDecimal("5.50"), true, "/images/suco.jpg", 1, now, now)
        );

        when(datasource.findAll(1)).thenReturn(list);

        var result = controller.findAll(1);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(p -> p.name().equals("Refrigerante Lata")));

        verify(datasource, times(1)).findAll(1);
    }

    @Test
    @DisplayName("Should propagate exception when datasource fails on findAll")
    void findAll_shouldPropagate_whenDatasourceFails() {
        when(datasource.findAll(99)).thenThrow(new RuntimeException("DB down"));

        assertThrows(RuntimeException.class, () -> controller.findAll(99));

        verify(datasource, times(1)).findAll(99);
    }

    // ==================== FIND ALL BY IDS TESTS ====================

    @Test
    @DisplayName("Should return products matching given IDs")
    void findAllByIds_shouldReturnProducts_matchingIds() {
        var id1 = UUID.randomUUID();
        var id2 = UUID.randomUUID();
        var now = LocalDateTime.now();
        var list = List.of(
            new ProductDTO(id1, "Coxinha", "Coxinha de frango", new BigDecimal("6.50"), true, "/images/coxinha.jpg", 2, now, now),
            new ProductDTO(id2, "Enroladinho", "Enroladinho de salsicha", new BigDecimal("5.00"), true, "/images/enroladinho.jpg", 2, now, now)
        );

        when(datasource.findAllByIds(List.of(id1, id2))).thenReturn(list);

        var result = controller.findAllByIds(List.of(id1, id2));

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(p -> p.id().equals(id1)));

        verify(datasource, times(1)).findAllByIds(List.of(id1, id2));
    }

    @Test
    @DisplayName("Should propagate exception when datasource fails on findAllByIds")
    void findAllByIds_shouldPropagate_whenDatasourceFails() {
        var id = UUID.randomUUID();
        when(datasource.findAllByIds(List.of(id))).thenThrow(new RuntimeException("DB down"));

        assertThrows(RuntimeException.class, () -> controller.findAllByIds(List.of(id)));

        verify(datasource, times(1)).findAllByIds(List.of(id));
    }

    // ==================== FIND BY ID TESTS ====================

    @Test
    @DisplayName("Should return product by ID - Coxinha")
    void findById_shouldReturnProduct_Coxinha() {
        var id = UUID.randomUUID();
        var now = LocalDateTime.now();
        var returned = new ProductDTO(id, "Coxinha", "Coxinha de frango frita", new BigDecimal("6.50"), true, "/images/coxinha.jpg", 1, now, now);

        when(datasource.findById(id)).thenReturn(returned);

        var result = controller.findById(id);

        assertNotNull(result);
        assertEquals(id, result.id());
        assertEquals("Coxinha", result.name());

        verify(datasource, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should propagate exception when product not found by ID")
    void findById_notFound_shouldPropagateProductNotFoundException() {
        var id = UUID.randomUUID();
        when(datasource.findById(id)).thenThrow(new IllegalArgumentException("Produto com ID %s não encontrado."));

        assertThrows(IllegalArgumentException.class, () -> controller.findById(id));

        verify(datasource, times(1)).findById(id);
    }

    // ==================== UPDATE TESTS ====================

    @Test
    @DisplayName("Should update product successfully - Salgar")
    void update_shouldReturnUpdatedProduct_Salvar() {
        var id = UUID.randomUUID();
        var dto = new UpdateProductDTO(id, "Salgar", "Salgado especial", new BigDecimal("7.50"), true, "/images/salgar.jpg", 3);
        var now = LocalDateTime.now();
        var returned = new ProductDTO(id, "Salgar", "Salgado especial", new BigDecimal("7.50"), true, "/images/salgar.jpg", 3, now, now);

        when(datasource.update(any())).thenReturn(returned);

        var result = controller.update(dto);

        assertNotNull(result);
        assertEquals("Salgar", result.name());

        verify(datasource, times(1)).update(any());
    }

    @Test
    @DisplayName("Should propagate exception when updating non-existent product")
    void update_notFound_shouldPropagateProductNotFound() {
        var id = UUID.randomUUID();
        var dto = new UpdateProductDTO(id, "NaoExiste", "Inexistente", new BigDecimal("0.00"), true, null, 99);

        when(datasource.update(any())).thenThrow(new IllegalArgumentException("Produto com ID %s não encontrado."));

        assertThrows(IllegalArgumentException.class, () -> controller.update(dto));

        verify(datasource, times(1)).update(any());
    }

    // ==================== DELETE TESTS ====================

    @Test
    @DisplayName("Should delete product successfully")
    void delete_shouldReturnDeletedProduct() {
        var id = UUID.randomUUID();
        var now = LocalDateTime.now();
        var foundProduct = new ProductDTO(id, "Promo Coxinha", "Promoção", new BigDecimal("3.00"), true, "/images/promo.jpg", 2, now, now);
        var deletedProduct = new ProductDTO(id, "Promo Coxinha", "Promoção", new BigDecimal("3.00"), true, "/images/promo.jpg", 2, now, now);

        when(datasource.findById(id)).thenReturn(foundProduct);
        when(datasource.delete(any(UUID.class))).thenReturn(deletedProduct);

        var result = controller.delete(id);

        assertNotNull(result);
        assertEquals(id, result.id());
        assertEquals("Promo Coxinha", result.name());
        assertEquals(new BigDecimal("3.00"), result.price());

        verify(datasource, times(1)).findById(id);
        verify(datasource, times(1)).delete(any(UUID.class));
    }

    @Test
    @DisplayName("Should propagate exception when deleting non-existent product")
    void delete_notFound_shouldPropagateProductNotFound() {
        var id = UUID.randomUUID();

        when(datasource.findById(id)).thenThrow(new IllegalArgumentException("Produto com ID " + id + " não encontrado."));

        var exception = assertThrows(IllegalArgumentException.class, () -> controller.delete(id));
        assertTrue(exception.getMessage().contains("Produto com ID"));
        assertTrue(exception.getMessage().contains("não encontrado"));

        verify(datasource, times(1)).findById(id);
        verify(datasource, never()).delete(any());
    }

    // ==================== ADDITIONAL EDGE CASE TESTS ====================

    @Test
    @DisplayName("Should handle null list in findAllByIds using defensive copy")
    void findAllByIds_shouldHandleNullList() {
        when(datasource.findAllByIds(Collections.emptyList())).thenReturn(Collections.emptyList());

        var result = controller.findAllByIds(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(datasource, times(1)).findAllByIds(Collections.emptyList());
    }

    @Test
    @DisplayName("Should handle empty list in findAllByIds")
    void findAllByIds_shouldHandleEmptyList() {
        when(datasource.findAllByIds(Collections.emptyList())).thenReturn(Collections.emptyList());

        var result = controller.findAllByIds(Collections.emptyList());

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(datasource, times(1)).findAllByIds(Collections.emptyList());
    }

    @Test
    @DisplayName("Should return empty list when no products match category")
    void findAll_shouldReturnEmptyList_whenNoProductsInCategory() {
        when(datasource.findAll(999)).thenReturn(Collections.emptyList());

        var result = controller.findAll(999);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(datasource, times(1)).findAll(999);
    }

    @Test
    @DisplayName("Should return empty list when no products match IDs")
    void findAllByIds_shouldReturnEmptyList_whenNoProductsMatch() {
        var id1 = UUID.randomUUID();
        var id2 = UUID.randomUUID();

        when(datasource.findAllByIds(List.of(id1, id2))).thenReturn(Collections.emptyList());

        var result = controller.findAllByIds(List.of(id1, id2));

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(datasource, times(1)).findAllByIds(List.of(id1, id2));
    }

    @Test
    @DisplayName("Should create product with composition/ingredients")
    void create_shouldHandleProductWithComposition() {
        var inventoryId1 = UUID.randomUUID();
        var inventoryId2 = UUID.randomUUID();

        var createDto = new CreateProductDTO(
            "X-Burger",
            "Hambúrguer completo",
            new BigDecimal("25.90"),
            true,
            "/images/xburger.jpg",
            1,
            List.of(
                new CreateInventoryProductDTO(inventoryId1, new BigDecimal("1.0")),
                new CreateInventoryProductDTO(inventoryId2, new BigDecimal("2.0"))
            )
        );

        var id = UUID.randomUUID();
        var now = LocalDateTime.now();
        var returned = new ProductDTO(id, "X-Burger", "Hambúrguer completo",
            new BigDecimal("25.90"), true, "/images/xburger.jpg", 1, now, now);

        when(datasource.create(any())).thenReturn(returned);

        // Mock inventory datasource for composition
        var inventoryDto1 = new br.com.fiap.fastfood.inventory.application.dtos.GetInventoryDTO(
            inventoryId1, "Pão", new br.com.fiap.fastfood.inventory.application.dtos.GetUnitDTO(1, "UN", "UN"),
            new BigDecimal("100"), new BigDecimal("10"), "", now, now);
        var inventoryDto2 = new br.com.fiap.fastfood.inventory.application.dtos.GetInventoryDTO(
            inventoryId2, "Carne", new br.com.fiap.fastfood.inventory.application.dtos.GetUnitDTO(2, "KG", "KG"),
            new BigDecimal("50"), new BigDecimal("5"), "", now, now);

        when(inventoryDatasource.getById(inventoryId1)).thenReturn(inventoryDto1);
        when(inventoryDatasource.getById(inventoryId2)).thenReturn(inventoryDto2);

        var result = controller.create(createDto);

        assertNotNull(result);
        assertEquals("X-Burger", result.name());
        assertEquals(new BigDecimal("25.90"), result.price());

        verify(datasource, times(1)).create(any());
        verify(inventoryDatasource, times(1)).getById(inventoryId1);
        verify(inventoryDatasource, times(1)).getById(inventoryId2);
    }

    @Test
    @DisplayName("Should create product without composition/ingredients")
    void create_shouldHandleProductWithoutComposition() {
        var createDto = new CreateProductDTO(
            "Água Mineral",
            "Água mineral 500ml",
            new BigDecimal("3.00"),
            true,
            "/images/agua.jpg",
            2,
            null
        );

        var id = UUID.randomUUID();
        var now = LocalDateTime.now();
        var returned = new ProductDTO(id, "Água Mineral", "Água mineral 500ml",
            new BigDecimal("3.00"), true, "/images/agua.jpg", 2, now, now);

        when(datasource.create(any())).thenReturn(returned);

        var result = controller.create(createDto);

        assertNotNull(result);
        assertEquals("Água Mineral", result.name());
        verify(datasource, times(1)).create(any());
    }

    @Test
    @DisplayName("Should create inactive product")
    void create_shouldHandleInactiveProduct() {
        var createDto = new CreateProductDTO(
            "Produto Inativo",
            "Produto fora de estoque",
            new BigDecimal("10.00"),
            false, // inactive
            "/images/inactive.jpg",
            1,
            null
        );

        var id = UUID.randomUUID();
        var now = LocalDateTime.now();
        var returned = new ProductDTO(id, "Produto Inativo", "Produto fora de estoque",
            new BigDecimal("10.00"), false, "/images/inactive.jpg", 1, now, now);

        when(datasource.create(any())).thenReturn(returned);

        var result = controller.create(createDto);

        assertNotNull(result);
        assertFalse(result.isActive());
        verify(datasource, times(1)).create(any());
    }

    @Test
    @DisplayName("Should update product changing active status")
    void update_shouldChangeActiveStatus() {
        var id = UUID.randomUUID();
        var dto = new UpdateProductDTO(id, "Produto", "Descrição",
            new BigDecimal("15.00"), false, "/images/produto.jpg", 1);
        var now = LocalDateTime.now();
        var returned = new ProductDTO(id, "Produto", "Descrição",
            new BigDecimal("15.00"), false, "/images/produto.jpg", 1, now, now);

        when(datasource.update(any())).thenReturn(returned);

        var result = controller.update(dto);

        assertNotNull(result);
        assertFalse(result.isActive());
        verify(datasource, times(1)).update(any());
    }

    @Test
    @DisplayName("Should update product price")
    void update_shouldUpdatePrice() {
        var id = UUID.randomUUID();
        var newPrice = new BigDecimal("99.99");
        var dto = new UpdateProductDTO(id, "Produto Premium", "Descrição premium",
            newPrice, true, "/images/premium.jpg", 1);
        var now = LocalDateTime.now();
        var returned = new ProductDTO(id, "Produto Premium", "Descrição premium",
            newPrice, true, "/images/premium.jpg", 1, now, now);

        when(datasource.update(any())).thenReturn(returned);

        var result = controller.update(dto);

        assertNotNull(result);
        assertEquals(0, newPrice.compareTo(result.price()));
        verify(datasource, times(1)).update(any());
    }

    @Test
    @DisplayName("Should findAll with null categoryId to get all products")
    void findAll_shouldReturnAllProducts_whenCategoryIdIsNull() {
        var now = LocalDateTime.now();
        var allProducts = List.of(
            new ProductDTO(UUID.randomUUID(), "Produto 1", "Desc 1", new BigDecimal("10.00"), true, "/img1.jpg", 1, now, now),
            new ProductDTO(UUID.randomUUID(), "Produto 2", "Desc 2", new BigDecimal("20.00"), true, "/img2.jpg", 2, now, now),
            new ProductDTO(UUID.randomUUID(), "Produto 3", "Desc 3", new BigDecimal("30.00"), true, "/img3.jpg", 3, now, now)
        );

        when(datasource.findAll(null)).thenReturn(allProducts);

        var result = controller.findAll(null);

        assertNotNull(result);
        assertEquals(3, result.size());
        verify(datasource, times(1)).findAll(null);
    }

    @Test
    @DisplayName("Should find product by specific UUID")
    void findById_shouldReturnSpecificProduct_byUUID() {
        var specificId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        var now = LocalDateTime.now();
        var product = new ProductDTO(specificId, "Produto Específico", "Descrição",
            new BigDecimal("50.00"), true, "/images/especifico.jpg", 1, now, now);

        when(datasource.findById(specificId)).thenReturn(product);

        var result = controller.findById(specificId);

        assertNotNull(result);
        assertEquals(specificId, result.id());
        assertEquals("Produto Específico", result.name());
        verify(datasource, times(1)).findById(specificId);
    }

    @Test
    @DisplayName("Should find multiple products by multiple IDs")
    void findAllByIds_shouldHandleMultipleIds() {
        var id1 = UUID.randomUUID();
        var id2 = UUID.randomUUID();
        var id3 = UUID.randomUUID();
        var now = LocalDateTime.now();

        var products = List.of(
            new ProductDTO(id1, "Produto 1", "Desc 1", new BigDecimal("10.00"), true, "/img1.jpg", 1, now, now),
            new ProductDTO(id2, "Produto 2", "Desc 2", new BigDecimal("20.00"), true, "/img2.jpg", 1, now, now),
            new ProductDTO(id3, "Produto 3", "Desc 3", new BigDecimal("30.00"), true, "/img3.jpg", 1, now, now)
        );

        when(datasource.findAllByIds(List.of(id1, id2, id3))).thenReturn(products);

        var result = controller.findAllByIds(List.of(id1, id2, id3));

        assertNotNull(result);
        assertEquals(3, result.size());
        verify(datasource, times(1)).findAllByIds(List.of(id1, id2, id3));
    }

    @Test
    @DisplayName("Should create product with minimum valid price")
    void create_shouldHandleMinimumPrice() {
        var createDto = new CreateProductDTO(
            "Produto Barato",
            "Produto com preço mínimo",
            new BigDecimal("0.01"),
            true,
            "/images/barato.jpg",
            1,
            null
        );

        var id = UUID.randomUUID();
        var now = LocalDateTime.now();
        var returned = new ProductDTO(id, "Produto Barato", "Produto com preço mínimo",
            new BigDecimal("0.01"), true, "/images/barato.jpg", 1, now, now);

        when(datasource.create(any())).thenReturn(returned);

        var result = controller.create(createDto);

        assertNotNull(result);
        assertEquals(0, new BigDecimal("0.01").compareTo(result.price()));
        verify(datasource, times(1)).create(any());
    }

    @Test
    @DisplayName("Should update product category")
    void update_shouldUpdateCategory() {
        var id = UUID.randomUUID();
        var dto = new UpdateProductDTO(id, "Produto", "Descrição",
            new BigDecimal("10.00"), true, "/images/produto.jpg", 5); // Different category
        var now = LocalDateTime.now();
        var returned = new ProductDTO(id, "Produto", "Descrição",
            new BigDecimal("10.00"), true, "/images/produto.jpg", 5, now, now);

        when(datasource.update(any())).thenReturn(returned);

        var result = controller.update(dto);

        assertNotNull(result);
        assertEquals(5, result.categoryId());
        verify(datasource, times(1)).update(any());
    }

    @Test
    @DisplayName("Should propagate exception when inventory datasource fails during product creation")
    void create_shouldPropagateException_whenInventoryDatasourceFails() {
        var inventoryId = UUID.randomUUID();
        var createDto = new CreateProductDTO(
            "Produto com Ingredientes",
            "Produto que precisa de inventário",
            new BigDecimal("20.00"),
            true,
            "/images/produto.jpg",
            1,
            List.of(new CreateInventoryProductDTO(inventoryId, new BigDecimal("1.0")))
        );

        var id = UUID.randomUUID();
        var now = LocalDateTime.now();
        var returned = new ProductDTO(id, "Produto com Ingredientes", "Produto que precisa de inventário",
            new BigDecimal("20.00"), true, "/images/produto.jpg", 1, now, now);

        when(datasource.create(any())).thenReturn(returned);
        when(inventoryDatasource.getById(inventoryId)).thenThrow(new RuntimeException("Inventory not found"));

        assertThrows(RuntimeException.class, () -> controller.create(createDto));

        verify(datasource, times(1)).create(any());
        verify(inventoryDatasource, times(1)).getById(inventoryId);
    }
}

