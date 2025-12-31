package br.com.fiap.fastfood.inventory.domain.use_cases;

import br.com.fiap.fastfood.inventory.application.dtos.CreateInventoryItemDTO;
import br.com.fiap.fastfood.inventory.application.gateways.InventoryGateway;
import br.com.fiap.fastfood.inventory.common.exceptions.EntityNotFoundException;
import br.com.fiap.fastfood.inventory.domain.entities.InventoryEntity;
import br.com.fiap.fastfood.inventory.domain.entities.UnitEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateInventoryItemUseCaseTest {

    private InventoryGateway gateway;
    private CreateInventoryItemUseCase useCase;

    @BeforeEach
    void setUp() {
        gateway = mock(InventoryGateway.class);
        useCase = new CreateInventoryItemUseCase(gateway);
    }

    @Test
    void shouldCreateInventoryItemWhenUnitExists() {
        Integer unitId = 1;
        CreateInventoryItemDTO dto = new CreateInventoryItemDTO(
            "Alface",
            unitId,
            new BigDecimal("10.00"),
            "resco"
        );

        UnitEntity unit = new UnitEntity(unitId, "Grama", "G");

        InventoryEntity expectedSavedItem = new InventoryEntity(
            UUID.randomUUID(),
            dto.name(),
            unit,
            BigDecimal.ZERO,
            dto.minimumQuantity(),
            dto.notes(),
            null,
            null
        );

        when(gateway.findUnitById(unitId)).thenReturn(Optional.of(unit));
        when(gateway.create(any(InventoryEntity.class))).thenReturn(expectedSavedItem);

        InventoryEntity result = useCase.run(dto);

        assertNotNull(result);
        assertEquals(dto.name(), result.getName());

        assertTrue(BigDecimal.ZERO.compareTo(result.getQuantity()) == 0,
            "Novo item deve sempre começar com quantidade ZERO (valor esperado: 0, valor atual: " + result.getQuantity() + ")");

        assertEquals(unit, result.getUnit());

        verify(gateway, times(1)).findUnitById(unitId);
        verify(gateway, times(1)).create(any(InventoryEntity.class));
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenUnitDoesNotExist() {
        Integer unitId = 99;
        CreateInventoryItemDTO dto = new CreateInventoryItemDTO(
            "Tomate",
            unitId,
            new BigDecimal("5.00"),
            "Notas"
        );

        when(gateway.findUnitById(unitId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
            EntityNotFoundException.class,
            () -> useCase.run(dto)
        );

        assertTrue(exception.getMessage().contains("Unidade não encontrada"));

        verify(gateway, never()).create(any());
    }
}
