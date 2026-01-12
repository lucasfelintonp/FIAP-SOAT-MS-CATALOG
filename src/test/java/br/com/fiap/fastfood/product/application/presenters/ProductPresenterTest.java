package br.com.fiap.fastfood.product.application.presenters;

import br.com.fiap.fastfood.product.application.dtos.ProductDTO;
import br.com.fiap.fastfood.product.domain.entities.ProductEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductPresenterTest {

    @Test
    void create_shouldMapEntityToDTO() {
        // Arrange
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        ProductEntity entity = new ProductEntity(
            id, "Burger", "Delicious", BigDecimal.TEN, true,
            "/img.jpg", 1, now, now, Optional.empty()
        );

        // Act
        ProductDTO result = ProductPresenter.create(entity);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.id());
        assertEquals("Burger", result.name());
        assertEquals("Delicious", result.description());
        assertEquals(0, BigDecimal.TEN.compareTo(result.price()));
        assertTrue(result.isActive());
        assertEquals("/img.jpg", result.imagePath());
        assertEquals(1, result.categoryId());
    }

    @Test
    void findById_shouldMapEntityToDTO() {
        // Arrange
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        ProductEntity entity = new ProductEntity(
            id, "Pizza", "Tasty", BigDecimal.valueOf(25.50), false,
            "/pizza.jpg", 2, now, now, Optional.of(now)
        );

        // Act
        ProductDTO result = ProductPresenter.findById(entity);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.id());
        assertEquals("Pizza", result.name());
        assertFalse(result.isActive());
    }

    @Test
    void findAll_shouldMapListOfEntitiesToListOfDTOs() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        List<ProductEntity> entities = List.of(
            new ProductEntity(UUID.randomUUID(), "Item1", "Desc1", BigDecimal.ONE, true, "/1.jpg", 1, now, now, Optional.empty()),
            new ProductEntity(UUID.randomUUID(), "Item2", "Desc2", BigDecimal.TEN, false, "/2.jpg", 2, now, now, Optional.empty())
        );

        // Act
        List<ProductDTO> result = ProductPresenter.findAll(entities);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Item1", result.get(0).name());
        assertEquals("Item2", result.get(1).name());
    }

    @Test
    void findAll_shouldHandleEmptyList() {
        // Arrange
        List<ProductEntity> entities = List.of();

        // Act
        List<ProductDTO> result = ProductPresenter.findAll(entities);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void update_shouldMapEntityToDTO() {
        // Arrange
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        ProductEntity entity = new ProductEntity(
            id, "Updated", "New Desc", BigDecimal.valueOf(15), true,
            "/new.jpg", 3, now, now, Optional.empty()
        );

        // Act
        ProductDTO result = ProductPresenter.update(entity);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.id());
        assertEquals("Updated", result.name());
        assertEquals("New Desc", result.description());
    }

    @Test
    void delete_shouldMapEntityToDTO() {
        // Arrange
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        ProductEntity entity = new ProductEntity(
            id, "Deleted", "Gone", BigDecimal.ZERO, false,
            "/deleted.jpg", 4, now, now, Optional.of(now)
        );

        // Act
        ProductDTO result = ProductPresenter.delete(entity);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.id());
        assertEquals("Deleted", result.name());
        assertFalse(result.isActive());
    }
}

