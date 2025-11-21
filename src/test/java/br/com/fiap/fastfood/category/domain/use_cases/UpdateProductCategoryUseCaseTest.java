package br.com.fiap.fastfood.category.domain.use_cases;

import br.com.fiap.fastfood.category.application.dtos.UpdateProductCategoryDTO;
import br.com.fiap.fastfood.category.application.gateways.ProductCategoryGateway;
import br.com.fiap.fastfood.category.domain.entities.ProductCategoryEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateProductCategoryUseCaseTest {

    private ProductCategoryGateway gateway;
    private UpdateProductCategoryUseCase useCase;

    @BeforeEach
    void setUp() {
        gateway = mock(ProductCategoryGateway.class);
        useCase = new UpdateProductCategoryUseCase(gateway);
    }

    @Test
    void shouldBuildEntityAndCallGateway() {
        UpdateProductCategoryDTO dto = new UpdateProductCategoryDTO(1, "name");
        ProductCategoryEntity expected = new ProductCategoryEntity(1, "name");
        when(gateway.update(any())).thenReturn(expected);

        var res = useCase.run(dto);

        assertEquals(expected, res);
        verify(gateway).update(any());
    }
}
