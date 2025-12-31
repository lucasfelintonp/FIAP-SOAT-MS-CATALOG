package br.com.fiap.fastfood.inventory.domain.use_cases;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.fiap.fastfood.inventory.application.dtos.CreateInventoryEntryDTO;
import br.com.fiap.fastfood.inventory.application.gateways.InventoryGateway;
import br.com.fiap.fastfood.inventory.domain.entities.InventoryEntity;
import br.com.fiap.fastfood.inventory.domain.entities.InventoryEntryEntity;
import br.com.fiap.fastfood.inventory.domain.entities.UnitEntity;

class CreateInventoryEntryUseCaseTest {

    private InventoryGateway gateway;
    private CreateInventoryEntryUseCase useCase;

    @BeforeEach
    void setUp() {
        gateway = mock(InventoryGateway.class);
        useCase = new CreateInventoryEntryUseCase(gateway);
    }

    @Test
    void shouldCreateInventoryEntryAndIncreaseQuantityWhenValid() {
        UUID inventoryId = UUID.randomUUID();
        BigDecimal initialQuantity = new BigDecimal("10.00");
        BigDecimal addedQuantity = new BigDecimal("5.00");
        BigDecimal expectedTotal = new BigDecimal("15.00");

        UnitEntity unit = new UnitEntity(1, "Quilograma", "KG");

        CreateInventoryEntryDTO dto = new CreateInventoryEntryDTO(
            inventoryId,
            addedQuantity,
            LocalDate.now().plusDays(30),
            LocalDate.now()
        );

        InventoryEntity currentInventory = new InventoryEntity(
            inventoryId,
            "Carne",
            unit,
            initialQuantity,
            new BigDecimal("5.00"),
            "Nota fiscal 123",
            null,
            null
        );

        InventoryEntryEntity entryToReturn = new InventoryEntryEntity(
            UUID.randomUUID(),
            currentInventory,
            addedQuantity,
            dto.expirationDate(),
            dto.entryDate()
        );

        when(gateway.getById(inventoryId)).thenReturn(currentInventory);
        when(gateway.createInventoryEntry(any(InventoryEntryEntity.class))).thenReturn(entryToReturn);

        InventoryEntryEntity result = useCase.run(dto);

        assertNotNull(result);
        assertEquals(expectedTotal, currentInventory.getQuantity(), "A quantidade total deve ser a soma da inicial com a nova entrada");

        verify(gateway, times(1)).getById(inventoryId);
        verify(gateway, times(1)).update(currentInventory);
        verify(gateway, times(1)).createInventoryEntry(any(InventoryEntryEntity.class));
    }

    @Test
    void shouldThrowExceptionWhenInventoryNotFound() {
        UUID inventoryId = UUID.randomUUID();
        CreateInventoryEntryDTO dto = new CreateInventoryEntryDTO(
            inventoryId,
            BigDecimal.TEN,
            LocalDate.now(),
            LocalDate.now()
        );

        when(gateway.getById(inventoryId)).thenThrow(new RuntimeException("Item de estoque não encontrado"));

        assertThrows(RuntimeException.class, () -> useCase.run(dto));

        verify(gateway, never()).update(any());
        verify(gateway, never()).createInventoryEntry(any());
    }
}
