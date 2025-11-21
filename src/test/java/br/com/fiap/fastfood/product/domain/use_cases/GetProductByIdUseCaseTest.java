package br.com.fiap.fastfood.product.domain.use_cases;

import br.com.fiap.fastfood.product.application.gateways.ProductGateway;
import br.com.fiap.fastfood.product.domain.entities.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetProductByIdUseCaseTest {

    private ProductGateway gateway;
    private GetProductByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        gateway = mock(ProductGateway.class);
        useCase = new GetProductByIdUseCase(gateway);
    }

    @Test
    void shouldReturnProductWhenFound() {
        UUID id = UUID.fromString("a3b5f3e0-1a2b-4c3d-8e9f-1234567890ab");
        ProductEntity expected = new ProductEntity(
            id,
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
        when(gateway.findById(id)).thenReturn(expected);

        ProductEntity result = useCase.run(id);

        assertNotNull(result);
        assertEquals(expected, result);
        verify(gateway, times(1)).findById(id);
    }
}
