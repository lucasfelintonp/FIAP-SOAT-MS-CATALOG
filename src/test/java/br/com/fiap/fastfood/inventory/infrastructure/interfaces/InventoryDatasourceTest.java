package br.com.fiap.fastfood.inventory.infrastructure.interfaces;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventoryDatasourceTest {

    @Test
    void mockInterfaceMethods() {
        InventoryDatasource ds = mock(InventoryDatasource.class);
        UUID id = UUID.fromString("a3b5f3e0-1a2b-4c3d-8e9f-1234567890ab");
        when(ds.getById(id)).thenReturn(null);

        assertNull(ds.getById(id));
        verify(ds).getById(id);
    }
}
