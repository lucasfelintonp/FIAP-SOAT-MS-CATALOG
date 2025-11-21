package br.com.fiap.fastfood.product.domain.use_cases;

import br.com.fiap.fastfood.product.application.gateways.ProductGateway;
import br.com.fiap.fastfood.product.domain.entities.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetProductsByIdsUseCaseTest {

    private ProductGateway gateway;
    private GetProductsByIdsUseCase useCase;

    @BeforeEach
    void setUp() {
        gateway = mock(ProductGateway.class);
        useCase = new GetProductsByIdsUseCase(gateway);
    }

    @Test
    void shouldReturnListFromGatewayByIds() {
        var id1 = java.util.UUID.fromString("a3b5f3e0-1a2b-4c3d-8e9f-1234567890ab");
        var id2 = java.util.UUID.fromString("b4c6d7f1-2b3c-4d5e-9f01-2345678901bc");

        ProductEntity e1 = new ProductEntity(
            id1,
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
        ProductEntity e2 = new ProductEntity(
            id2,
            "Chicken Sandwich",
            "Sanduíche de frango crocante com maionese e alface",
            new BigDecimal("21.50"),
            true,
            "/images/chicken_sandwich.jpg",
            1,
            null,
            null,
            null
        );

        when(gateway.findAllByIds(List.of(id1, id2))).thenReturn(List.of(e1, e2));

        var result = useCase.run(List.of(id1, id2));

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(e1));
        verify(gateway, times(1)).findAllByIds(List.of(id1, id2));
    }

    @Test
    void shouldReturnEmptyListWhenGatewayReturnsEmpty() {
        var id1 = java.util.UUID.fromString("a3b5f3e0-1a2b-4c3d-8e9f-1234567890ab");
        var id2 = java.util.UUID.fromString("b4c6d7f1-2b3c-4d5e-9f01-2345678901bc");

        when(gateway.findAllByIds(List.of(id1, id2))).thenReturn(List.of());

        var result = useCase.run(List.of(id1, id2));

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(gateway, times(1)).findAllByIds(List.of(id1, id2));
    }
}
