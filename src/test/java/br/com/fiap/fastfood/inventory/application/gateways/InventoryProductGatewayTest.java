package br.com.fiap.fastfood.inventory.application.gateways;

import br.com.fiap.fastfood.inventory.application.dtos.GetInventoryDTO;
import br.com.fiap.fastfood.inventory.application.dtos.GetInventoryProductDTO;
import br.com.fiap.fastfood.inventory.application.dtos.GetUnitDTO;
import br.com.fiap.fastfood.inventory.domain.entities.InventoryProductsEntity;
import br.com.fiap.fastfood.inventory.infrastructure.interfaces.InventoryProductsDatasource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryProductGatewayTest {

    @Mock
    private InventoryProductsDatasource datasource;

    private InventoryProductGateway gateway;

    private GetInventoryProductDTO productInventoryDTO;

    @BeforeEach
    void setUp() {
        gateway = new InventoryProductGateway(datasource);

        var unitDTO = new GetUnitDTO(1, "Grama", "G");

        var inventoryDTO = new GetInventoryDTO(
            UUID.randomUUID(),
            "Sal",
            unitDTO,
            new BigDecimal("1.00"),
            new BigDecimal("1.00"),
            "Refinado",
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        productInventoryDTO = new GetInventoryProductDTO(
            UUID.randomUUID(),
            UUID.randomUUID(),
            inventoryDTO,
            new BigDecimal("1.00"),
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    @Test
    void getInventoryProductByProductId_shouldMapCorrectEntityStructure() {
        UUID productId = productInventoryDTO.productId();
        when(datasource.getInventoryProductByProductId(productId))
            .thenReturn(List.of(productInventoryDTO));

        List<InventoryProductsEntity> result = gateway.getInventoryProductByProductId(productId);

        assertFalse(result.isEmpty());
        InventoryProductsEntity entity = result.get(0);

        assertEquals(productInventoryDTO.id(), entity.getId());
        assertEquals(productId, entity.getProductId());

        assertEquals(new BigDecimal("1.00"), entity.getQuantity());

        assertEquals("Sal", entity.getInventory().getName());
        assertEquals("G", entity.getInventory().getUnit().getAbbreviation());

        verify(datasource, times(1)).getInventoryProductByProductId(productId);
    }

    @Test
    void getInventoryProductByInventoryId_shouldMapCorrectEntityStructure() {
        UUID inventoryId = productInventoryDTO.inventory().id();
        when(datasource.getInventoryProductByInventoryId(inventoryId))
            .thenReturn(List.of(productInventoryDTO));

        List<InventoryProductsEntity> result = gateway.getInventoryProductByInventoryId(inventoryId);

        assertFalse(result.isEmpty());
        assertEquals(inventoryId, result.get(0).getInventory().getId());

        verify(datasource, times(1)).getInventoryProductByInventoryId(inventoryId);
    }

    @Test
    void shouldReturnEmptyList_whenDatasourceReturnsEmpty() {
        UUID productId = UUID.randomUUID();
        when(datasource.getInventoryProductByProductId(productId)).thenReturn(List.of());

        List<InventoryProductsEntity> result = gateway.getInventoryProductByProductId(productId);

        assertTrue(result.isEmpty());
        verify(datasource, times(1)).getInventoryProductByProductId(productId);
    }
}
