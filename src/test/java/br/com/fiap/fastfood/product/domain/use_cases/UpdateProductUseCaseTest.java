package br.com.fiap.fastfood.product.domain.use_cases;

import br.com.fiap.fastfood.product.application.dtos.UpdateProductDTO;
import br.com.fiap.fastfood.product.application.gateways.ProductGateway;
import br.com.fiap.fastfood.product.domain.entities.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateProductUseCaseTest {

    private ProductGateway gateway;
    private UpdateProductUseCase useCase;

    @BeforeEach
    void setUp() {
        gateway = mock(ProductGateway.class);
        useCase = new UpdateProductUseCase(gateway);
    }

    @Test
    void shouldCallGatewayUpdateAndReturnEntity() {
        UUID id = UUID.fromString("b4c6d7f1-2b3c-4d5e-9f01-2345678901bc");
        UpdateProductDTO dto = new UpdateProductDTO(
                id,
                "Chicken Sandwich",
                "Sanduíche de frango crocante com maionese e alface",
                new BigDecimal("21.50"),
                true,
                "/images/chicken_sandwich.jpg",
                1
        );

        ProductEntity expected = new ProductEntity(
                id,
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
        when(gateway.update(any())).thenReturn(expected);

        ProductEntity result = useCase.run(dto);

        assertNotNull(result);
        assertEquals(expected, result);

        ArgumentCaptor<ProductEntity> captor = ArgumentCaptor.forClass(ProductEntity.class);
        verify(gateway, times(1)).update(captor.capture());
        ProductEntity captured = captor.getValue();
        assertEquals(id, captured.id());
        assertEquals("Chicken Sandwich", captured.name());
    }
}
