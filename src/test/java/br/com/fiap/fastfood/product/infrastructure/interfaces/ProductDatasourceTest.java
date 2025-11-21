package br.com.fiap.fastfood.product.infrastructure.interfaces;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductDatasourceTest {

    @Test
    void mockInterfaceMethods() {
        ProductDatasource ds = mock(ProductDatasource.class);
        UUID id = UUID.fromString("a3b5f3e0-1a2b-4c3d-8e9f-1234567890ab");
        when(ds.findById(id)).thenReturn(null);

        assertNull(ds.findById(id));
        verify(ds).findById(id);
    }
}
