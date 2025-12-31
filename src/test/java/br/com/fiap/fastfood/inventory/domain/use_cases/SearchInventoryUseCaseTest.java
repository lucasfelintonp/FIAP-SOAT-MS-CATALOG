package br.com.fiap.fastfood.inventory.domain.use_cases;

import br.com.fiap.fastfood.inventory.application.gateways.InventoryGateway;
import br.com.fiap.fastfood.inventory.domain.entities.InventoryEntity;
import br.com.fiap.fastfood.inventory.domain.entities.UnitEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SearchInventoryUseCaseTest {

    private InventoryGateway gateway;
    private SearchInventoryUseCase useCase;

    @BeforeEach
    void setUp() {
        gateway = mock(InventoryGateway.class);
        useCase = new SearchInventoryUseCase(gateway);
    }

    @Test
    void shouldReturnListOfInventoryItems() {
        UnitEntity unit = new UnitEntity(1, "Unidade", "UN");
        InventoryEntity item1 = new InventoryEntity(UUID.randomUUID(), "Item 1", unit,
            new BigDecimal("10.00"), BigDecimal.ONE, "", null, null);
        InventoryEntity item2 = new InventoryEntity(UUID.randomUUID(), "Item 2", unit,
            new BigDecimal("5.00"), BigDecimal.ONE, "", null, null);

        List<InventoryEntity> expectedList = List.of(item1, item2);

        when(gateway.findAll()).thenReturn(expectedList);

        List<InventoryEntity> result = useCase.run();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedList, result);
        verify(gateway, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoItemsFound() {
        when(gateway.findAll()).thenReturn(List.of());

        List<InventoryEntity> result = useCase.run();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(gateway, times(1)).findAll();
    }
}
