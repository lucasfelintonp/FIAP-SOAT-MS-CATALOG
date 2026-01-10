package br.com.fiap.fastfood.inventory.domain.use_cases;

import br.com.fiap.fastfood.inventory.application.dtos.ProductsQuantityDTO;
import br.com.fiap.fastfood.inventory.application.gateways.InventoryGateway;
import br.com.fiap.fastfood.inventory.application.gateways.InventoryProductGateway;
import br.com.fiap.fastfood.inventory.common.exceptions.InvalidQuantityException;
import br.com.fiap.fastfood.inventory.domain.entities.InventoryEntity;
import br.com.fiap.fastfood.inventory.domain.entities.InventoryProductsEntity;
import br.com.fiap.fastfood.inventory.domain.entities.UnitEntity;
import br.com.fiap.fastfood.product.application.gateways.ProductGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiscountInventoryValidationTest {

    @Mock
    private InventoryProductGateway inventoryProductGateway;

    @Mock
    private InventoryGateway inventoryGateway;

    @Mock
    private ProductGateway productGateway;

    private DiscountInventoryItemsByProductsUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new DiscountInventoryItemsByProductsUseCase(
            inventoryProductGateway,
            inventoryGateway,
            productGateway
        );
    }

    @Test
    void run_withNullQuantity_shouldThrowInvalidQuantityException() {
        // Arrange
        UUID productId = UUID.randomUUID();
        ProductsQuantityDTO dto = new ProductsQuantityDTO(productId, null);

        // Act & Assert
        assertThrows(InvalidQuantityException.class, () -> useCase.run(List.of(dto)));
        verify(inventoryGateway, never()).update(any());
    }

    @Test
    void run_withNegativeQuantity_shouldThrowInvalidQuantityException() {
        // Arrange
        UUID productId = UUID.randomUUID();
        ProductsQuantityDTO dto = new ProductsQuantityDTO(productId, BigDecimal.valueOf(-5.0));

        // Act & Assert
        assertThrows(InvalidQuantityException.class, () -> useCase.run(List.of(dto)));
        verify(inventoryGateway, never()).update(any());
    }

    @Test
    void run_withZeroQuantity_shouldThrowInvalidQuantityException() {
        // Arrange
        UUID productId = UUID.randomUUID();
        ProductsQuantityDTO dto = new ProductsQuantityDTO(productId, BigDecimal.ZERO);

        // Act & Assert
        assertThrows(InvalidQuantityException.class, () -> useCase.run(List.of(dto)));
        verify(inventoryGateway, never()).update(any());
    }

    @Test
    void run_withInsufficientStock_shouldThrowInvalidQuantityException() {
        // Arrange
        UUID productId = UUID.randomUUID();
        UUID inventoryId = UUID.randomUUID();

        ProductsQuantityDTO dto = new ProductsQuantityDTO(productId, BigDecimal.valueOf(20.0));

        UnitEntity unit = new UnitEntity(1, "Kilogram", "kg");
        InventoryEntity inventory = new InventoryEntity(
            inventoryId,
            "Tomato",
            unit,
            BigDecimal.valueOf(10.0), // Only 10 available
            BigDecimal.valueOf(5.0),
            "Notes",
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        InventoryProductsEntity relation = new InventoryProductsEntity(
            UUID.randomUUID(),
            productId,
            inventory,
            BigDecimal.ONE, // 1:1 ratio
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        when(inventoryProductGateway.getInventoryProductByProductId(productId))
            .thenReturn(List.of(relation));

        // Act & Assert - trying to discount 20 but only 10 available
        assertThrows(InvalidQuantityException.class, () -> useCase.run(List.of(dto)));
        verify(inventoryGateway, never()).update(any());
    }

    @Test
    void run_withNullInventoryQuantity_shouldThrowInvalidQuantityException() {
        // Arrange
        UUID productId = UUID.randomUUID();
        UUID inventoryId = UUID.randomUUID();

        ProductsQuantityDTO dto = new ProductsQuantityDTO(productId, BigDecimal.valueOf(10.0));

        UnitEntity unit = new UnitEntity(1, "Kilogram", "kg");
        InventoryEntity inventory = new InventoryEntity(
            inventoryId,
            "Tomato",
            unit,
            null, // Null quantity
            BigDecimal.valueOf(5.0),
            "Notes",
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        InventoryProductsEntity relation = new InventoryProductsEntity(
            UUID.randomUUID(),
            productId,
            inventory,
            BigDecimal.ONE,
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        when(inventoryProductGateway.getInventoryProductByProductId(productId))
            .thenReturn(List.of(relation));

        // Act & Assert
        assertThrows(InvalidQuantityException.class, () -> useCase.run(List.of(dto)));
        verify(inventoryGateway, never()).update(any());
    }

    @Test
    void run_reachingZeroStock_shouldDisableProducts() {
        // Arrange
        UUID productId = UUID.randomUUID();
        UUID inventoryId = UUID.randomUUID();

        ProductsQuantityDTO dto = new ProductsQuantityDTO(productId, BigDecimal.valueOf(10.0));

        UnitEntity unit = new UnitEntity(1, "Kilogram", "kg");
        InventoryEntity inventory = new InventoryEntity(
            inventoryId,
            "Tomato",
            unit,
            BigDecimal.valueOf(10.0), // Exactly the amount needed
            BigDecimal.valueOf(5.0),
            "Notes",
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        InventoryProductsEntity relation = new InventoryProductsEntity(
            UUID.randomUUID(),
            productId,
            inventory,
            BigDecimal.ONE,
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        when(inventoryProductGateway.getInventoryProductByProductId(productId))
            .thenReturn(List.of(relation));
        when(inventoryProductGateway.getInventoryProductByInventoryId(inventoryId))
            .thenReturn(List.of(relation));

        // Act
        useCase.run(List.of(dto));

        // Assert
        verify(inventoryGateway, times(1)).update(any());
        verify(productGateway, times(1)).disableProduct(productId);
    }

    @Test
    void run_withValidQuantity_shouldUpdateInventory() {
        // Arrange
        UUID productId = UUID.randomUUID();
        UUID inventoryId = UUID.randomUUID();

        ProductsQuantityDTO dto = new ProductsQuantityDTO(productId, BigDecimal.valueOf(5.0));

        UnitEntity unit = new UnitEntity(1, "Kilogram", "kg");
        InventoryEntity inventory = new InventoryEntity(
            inventoryId,
            "Tomato",
            unit,
            BigDecimal.valueOf(20.0),
            BigDecimal.valueOf(5.0),
            "Notes",
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        InventoryProductsEntity relation = new InventoryProductsEntity(
            UUID.randomUUID(),
            productId,
            inventory,
            BigDecimal.ONE,
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        when(inventoryProductGateway.getInventoryProductByProductId(productId))
            .thenReturn(List.of(relation));

        // Act
        useCase.run(List.of(dto));

        // Assert
        verify(inventoryGateway, times(1)).update(any());
        verify(productGateway, never()).disableProduct(any());
    }
}

