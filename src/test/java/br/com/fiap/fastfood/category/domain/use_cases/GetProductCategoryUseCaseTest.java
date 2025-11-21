package br.com.fiap.fastfood.category.domain.use_cases;

import br.com.fiap.fastfood.category.application.gateways.ProductCategoryGateway;
import br.com.fiap.fastfood.category.domain.entities.ProductCategoryEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetProductCategoryUseCaseTest {

    private ProductCategoryGateway gateway;
    private GetProductCategoryUseCase useCase;

    @BeforeEach
    void setUp() {
        gateway = mock(ProductCategoryGateway.class);
        useCase = new GetProductCategoryUseCase(gateway);
    }

    @Test
    void shouldReturnCategory() {
        when(gateway.findById(1)).thenReturn(new ProductCategoryEntity(1, "Bebida"));
        var res = useCase.run(1);
        assertEquals(1, res.id());
        verify(gateway).findById(1);
    }
}
