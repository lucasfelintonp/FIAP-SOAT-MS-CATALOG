package br.com.fiap.fastfood.category.domain.use_cases;

import br.com.fiap.fastfood.category.application.gateways.ProductCategoryGateway;
import br.com.fiap.fastfood.category.domain.entities.ProductCategoryEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetAllProductCategoriesUseCaseTest {

    private ProductCategoryGateway gateway;
    private GetAllProductCategoriesUseCase useCase;

    @BeforeEach
    void setUp() {
        gateway = mock(ProductCategoryGateway.class);
        useCase = new GetAllProductCategoriesUseCase(gateway);
    }

    @Test
    void shouldReturnList() {
        when(gateway.findAll()).thenReturn(List.of(
            new ProductCategoryEntity(
                2,
                "Bebida"
            ),
            new ProductCategoryEntity(
                3,
                "Sobremesa"
            )
        ));
        var res = useCase.run();
        assertFalse(res.isEmpty());
        verify(gateway).findAll();
    }
}
