package br.com.fiap.fastfood.product.domain.use_cases;

import br.com.fiap.fastfood.product.application.dtos.CreateProductDTO;
import br.com.fiap.fastfood.product.application.gateways.ProductGateway;
import br.com.fiap.fastfood.product.common.exceptions.ProductInvalidRequestException;
import br.com.fiap.fastfood.product.domain.entities.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateProductUseCaseTest {

    private ProductGateway gateway;
    private CreateProductUseCase useCase;

    @BeforeEach
    void setUp() {
        gateway = mock(ProductGateway.class);
        useCase = new CreateProductUseCase(gateway);
    }

    @Test
    void shouldCreateProductWhenValid() {
        CreateProductDTO dto = new CreateProductDTO(
            "Cheeseburger",
            "Hambúrguer com queijo, alface e molho especial",
            new BigDecimal("19.90"),
            true,
            "/images/cheeseburger.jpg",
            1,
            null
        );
        ProductEntity returned = new ProductEntity(
            null,
            "Cheeseburger",
            "Hambúrguer com queijo, alface e molho especial",
            new BigDecimal("19.90"),
            true,
            "/images/cheeseburger.jpg",
            1,
            null,
            null,
            null
        );
        when(gateway.create(any())).thenReturn(returned);

        ProductEntity result = useCase.run(dto);

        assertNotNull(result);
        assertEquals(returned, result);
        verify(gateway, times(1)).create(any());
    }

    @Test
    void shouldThrowWhenInvalidName() {
        CreateProductDTO dto = new CreateProductDTO(
            "",
            "Hambúrguer com queijo, alface e molho especial",
            new BigDecimal("19.90"),
            true,
            "/images/cheeseburger.jpg",
            1,
            null
        );

        assertThrows(ProductInvalidRequestException.class, () -> useCase.run(dto));
    }
}
