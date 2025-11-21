package br.com.fiap.fastfood.category.infrastructure.interfaces;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductCategoryDatasourceTest {

    @Test
    void placeholder() {
        assertTrue(true);
    }

    @Test
    void mockInterface() {
        ProductCategoryDatasource ds = mock(ProductCategoryDatasource.class);
        when(ds.findAll()).thenReturn(java.util.List.of());

        var res = ds.findAll();
        assertNotNull(res);
        verify(ds).findAll();
    }
}
