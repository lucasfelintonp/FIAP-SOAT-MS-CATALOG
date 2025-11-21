package br.com.fiap.fastfood.category.domain.use_cases;

import br.com.fiap.fastfood.category.application.gateways.ProductCategoryGateway;
import br.com.fiap.fastfood.category.domain.entities.ProductCategoryEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteProductCategoryUseCaseTest {

    private ProductCategoryGateway gateway;
    private DeleteProductCategoryUseCase useCase;

    @BeforeEach
    void setUp() {
        gateway = mock(ProductCategoryGateway.class);
        useCase = new DeleteProductCategoryUseCase(gateway);
    }

    @Test
    void shouldFindAndDelete() {
        var entity = new ProductCategoryEntity(
            4,
            "Acompanhamentos"
        );
        when(gateway.findById(4)).thenReturn(entity);
        doNothing().when(gateway).delete(entity);

        var res = useCase.run(4);
        assertEquals(entity, res);
        verify(gateway).findById(4);
        verify(gateway).delete(entity);
    }
}
