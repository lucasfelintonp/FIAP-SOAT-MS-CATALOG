package br.com.fiap.fastfood.product.domain.use_cases;

import br.com.fiap.fastfood.inventory.application.dtos.CreateInventoryProductDTO;
import br.com.fiap.fastfood.product.application.dtos.CreateProductDTO;
import br.com.fiap.fastfood.product.application.gateways.ProductGateway;
import br.com.fiap.fastfood.product.common.exceptions.ProductInvalidRequestException;
import br.com.fiap.fastfood.product.domain.entities.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateProductUseCaseValidationTest {

    @Mock
    private ProductGateway productGateway;

    private CreateProductUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateProductUseCase(productGateway);
    }

    @Test
    void run_withValidData_shouldCreateProduct() {
        // Arrange
        CreateProductDTO dto = new CreateProductDTO(
            "Test Product",
            "Test Description",
            BigDecimal.valueOf(10.50),
            true,
            "/image.jpg",
            1,
            List.of()
        );

        ProductEntity expectedEntity = new ProductEntity(
            UUID.randomUUID(),
            "Test Product",
            "Test Description",
            BigDecimal.valueOf(10.50),
            true,
            "/image.jpg",
            1,
            null,
            null,
            null
        );

        when(productGateway.create(any(ProductEntity.class))).thenReturn(expectedEntity);

        // Act
        ProductEntity result = useCase.run(dto);

        // Assert
        assertNotNull(result);
        verify(productGateway, times(1)).create(any(ProductEntity.class));
    }

    @Test
    void run_withNullName_shouldThrowException() {
        // Arrange
        CreateProductDTO dto = new CreateProductDTO(
            null,
            "Description",
            BigDecimal.valueOf(10.50),
            true,
            "/image.jpg",
            1,
            List.of()
        );

        // Act & Assert
        assertThrows(ProductInvalidRequestException.class, () -> useCase.run(dto));
        verify(productGateway, never()).create(any());
    }

    @Test
    void run_withBlankName_shouldThrowException() {
        // Arrange
        CreateProductDTO dto = new CreateProductDTO(
            "   ",
            "Description",
            BigDecimal.valueOf(10.50),
            true,
            "/image.jpg",
            1,
            List.of()
        );

        // Act & Assert
        assertThrows(ProductInvalidRequestException.class, () -> useCase.run(dto));
        verify(productGateway, never()).create(any());
    }

    @Test
    void run_withNullDescription_shouldThrowException() {
        // Arrange
        CreateProductDTO dto = new CreateProductDTO(
            "Product Name",
            null,
            BigDecimal.valueOf(10.50),
            true,
            "/image.jpg",
            1,
            List.of()
        );

        // Act & Assert
        assertThrows(ProductInvalidRequestException.class, () -> useCase.run(dto));
        verify(productGateway, never()).create(any());
    }

    @Test
    void run_withBlankDescription_shouldThrowException() {
        // Arrange
        CreateProductDTO dto = new CreateProductDTO(
            "Product Name",
            "",
            BigDecimal.valueOf(10.50),
            true,
            "/image.jpg",
            1,
            List.of()
        );

        // Act & Assert
        assertThrows(ProductInvalidRequestException.class, () -> useCase.run(dto));
        verify(productGateway, never()).create(any());
    }

    @Test
    void run_withNullPrice_shouldThrowException() {
        // Arrange
        CreateProductDTO dto = new CreateProductDTO(
            "Product Name",
            "Description",
            null,
            true,
            "/image.jpg",
            1,
            List.of()
        );

        // Act & Assert
        assertThrows(ProductInvalidRequestException.class, () -> useCase.run(dto));
        verify(productGateway, never()).create(any());
    }

    @Test
    void run_withNegativePrice_shouldThrowException() {
        // Arrange
        CreateProductDTO dto = new CreateProductDTO(
            "Product Name",
            "Description",
            BigDecimal.valueOf(-10.00),
            true,
            "/image.jpg",
            1,
            List.of()
        );

        // Act & Assert
        assertThrows(ProductInvalidRequestException.class, () -> useCase.run(dto));
        verify(productGateway, never()).create(any());
    }

    @Test
    void run_withZeroPrice_shouldThrowException() {
        // Arrange
        CreateProductDTO dto = new CreateProductDTO(
            "Product Name",
            "Description",
            BigDecimal.ZERO,
            true,
            "/image.jpg",
            1,
            List.of()
        );

        // Act & Assert
        assertThrows(ProductInvalidRequestException.class, () -> useCase.run(dto));
        verify(productGateway, never()).create(any());
    }

    @Test
    void run_withNullIsActive_shouldThrowException() {
        // Arrange
        CreateProductDTO dto = new CreateProductDTO(
            "Product Name",
            "Description",
            BigDecimal.valueOf(10.50),
            null,
            "/image.jpg",
            1,
            List.of()
        );

        // Act & Assert
        assertThrows(ProductInvalidRequestException.class, () -> useCase.run(dto));
        verify(productGateway, never()).create(any());
    }

    @Test
    void run_withNullCategoryId_shouldThrowException() {
        // Arrange
        CreateProductDTO dto = new CreateProductDTO(
            "Product Name",
            "Description",
            BigDecimal.valueOf(10.50),
            true,
            "/image.jpg",
            null,
            List.of()
        );

        // Act & Assert
        assertThrows(ProductInvalidRequestException.class, () -> useCase.run(dto));
        verify(productGateway, never()).create(any());
    }
}

