package br.com.fiap.fastfood.inventory.infrastructure.database.adapters;

import br.com.fiap.fastfood.inventory.application.dtos.GetInventoryProductDTO;
import br.com.fiap.fastfood.inventory.infrastructure.database.entities.InventoryEntityJPA;
import br.com.fiap.fastfood.inventory.infrastructure.database.entities.InventoryProductsEntityJPA;
import br.com.fiap.fastfood.inventory.infrastructure.database.entities.UnitEntityJPA;
import br.com.fiap.fastfood.inventory.infrastructure.database.repositories.InventoryProductsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryProductsAdapterTest {

    @Mock
    private InventoryProductsRepository repository;

    @InjectMocks
    private InventoryProductsAdapter adapter;

    private UUID productId;
    private UUID inventoryId;
    private InventoryProductsEntityJPA inventoryProductEntity;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        inventoryId = UUID.randomUUID();

        UnitEntityJPA unit = new UnitEntityJPA();
        unit.setId(1);
        unit.setName("Unidade");
        unit.setAbbreviation("UN");

        InventoryEntityJPA inventory = new InventoryEntityJPA();
        inventory.setId(inventoryId);
        inventory.setName("Pão de Hambúrguer");
        inventory.setUnit(unit);
        inventory.setQuantity(new BigDecimal("100.0"));
        inventory.setMinimumQuantity(new BigDecimal("20.0"));
        inventory.setCreatedAt(LocalDateTime.now());

        inventoryProductEntity = new InventoryProductsEntityJPA();
        inventoryProductEntity.setId(UUID.randomUUID());
        inventoryProductEntity.setProductId(productId);
        inventoryProductEntity.setInventory(inventory);
        inventoryProductEntity.setQuantity(new BigDecimal("1.0"));
        inventoryProductEntity.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void getInventoryProductByProductId_success_shouldReturnList() {
        when(repository.findByProductId(productId)).thenReturn(List.of(inventoryProductEntity));

        List<GetInventoryProductDTO> result = adapter.getInventoryProductByProductId(productId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(productId, result.get(0).productId());
        assertEquals(inventoryId, result.get(0).inventory().id());

        assertEquals(0, new BigDecimal("1.0").compareTo(result.get(0).quantity()));

        verify(repository, times(1)).findByProductId(productId);
    }

    @Test
    void getInventoryProductByProductId_empty_shouldReturnEmptyList() {
        UUID randomId = UUID.randomUUID();
        when(repository.findByProductId(randomId)).thenReturn(List.of());

        List<GetInventoryProductDTO> result = adapter.getInventoryProductByProductId(randomId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findByProductId(randomId);
    }

    @Test
    void getInventoryProductByInventoryId_success_shouldReturnList() {
        when(repository.findByInventoryId(inventoryId)).thenReturn(List.of(inventoryProductEntity));

        List<GetInventoryProductDTO> result = adapter.getInventoryProductByInventoryId(inventoryId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(inventoryId, result.get(0).inventory().id());

        assertEquals("Pão de Hambúrguer", result.get(0).inventory().name());
        assertEquals(0, new BigDecimal("100.0").compareTo(result.get(0).inventory().quantity()));

        verify(repository, times(1)).findByInventoryId(inventoryId);
    }

    @Test
    void getInventoryProductByInventoryId_empty_shouldReturnEmptyList() {
        UUID randomId = UUID.randomUUID();
        when(repository.findByInventoryId(randomId)).thenReturn(List.of());

        List<GetInventoryProductDTO> result = adapter.getInventoryProductByInventoryId(randomId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findByInventoryId(randomId);
    }
}
