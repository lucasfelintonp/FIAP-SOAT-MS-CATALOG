package br.com.fiap.fastfood.category.application.presenters;

import br.com.fiap.fastfood.category.application.dtos.ProductCategoryDTO;
import br.com.fiap.fastfood.category.domain.entities.ProductCategoryEntity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductCategoryPresenterTest {

    @Test
    void create_shouldMapEntityToDTO() {
        // Arrange
        ProductCategoryEntity entity = new ProductCategoryEntity(1, "Bebidas");

        // Act
        ProductCategoryDTO result = ProductCategoryPresenter.create(entity);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.id());
        assertEquals("Bebidas", result.name());
    }

    @Test
    void create_shouldHandleNullId() {
        // Arrange
        ProductCategoryEntity entity = new ProductCategoryEntity(null, "Test");

        // Act
        ProductCategoryDTO result = ProductCategoryPresenter.create(entity);

        // Assert
        assertNotNull(result);
        assertNull(result.id());
        assertEquals("Test", result.name());
    }

    @Test
    void findAll_shouldMapListOfEntitiesToListOfDTOs() {
        // Arrange
        List<ProductCategoryEntity> entities = List.of(
            new ProductCategoryEntity(1, "Bebidas"),
            new ProductCategoryEntity(2, "Lanches"),
            new ProductCategoryEntity(3, "Sobremesas")
        );

        // Act
        List<ProductCategoryDTO> result = ProductCategoryPresenter.findAll(entities);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Bebidas", result.get(0).name());
        assertEquals("Lanches", result.get(1).name());
        assertEquals("Sobremesas", result.get(2).name());
    }

    @Test
    void findAll_shouldHandleEmptyList() {
        // Arrange
        List<ProductCategoryEntity> entities = List.of();

        // Act
        List<ProductCategoryDTO> result = ProductCategoryPresenter.findAll(entities);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}

