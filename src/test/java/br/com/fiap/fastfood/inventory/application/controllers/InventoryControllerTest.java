package br.com.fiap.fastfood.inventory.application.controllers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.fastfood.inventory.application.dtos.CreateInventoryItemDTO;
import br.com.fiap.fastfood.inventory.application.dtos.GetInventoryDTO;
import br.com.fiap.fastfood.inventory.application.dtos.GetUnitDTO;
import br.com.fiap.fastfood.inventory.infrastructure.interfaces.InventoryDatasource;

@ExtendWith(MockitoExtension.class)
class InventoryControllerTest {

    @Mock
    private InventoryDatasource datasource;

    private InventoryController controller;

    private GetUnitDTO unitDTO;

    @BeforeEach
    void setUp() {
        controller = new InventoryController(datasource);
        unitDTO = new GetUnitDTO(1, "Unidade", "UN");
    }

    @Test
    void searchInventory_shouldReturnInventoryList() {
        var now = LocalDateTime.now();
        var inventoryList = List.of(
            new GetInventoryDTO(UUID.randomUUID(), "Pão de Hambúrguer", unitDTO, new BigDecimal("100.00"), new BigDecimal("10.00"), "Frescos", now, now),
            new GetInventoryDTO(UUID.randomUUID(), "Carne Bovida", unitDTO, new BigDecimal("50.00"), new BigDecimal("5.00"), "Congelados", now, now)
        );

        when(datasource.findAll()).thenReturn(inventoryList);

        var result = controller.searchInventory();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Pão de Hambúrguer", result.get(0).name());
        verify(datasource, times(1)).findAll();
    }

    @Test
    void searchInventory_shouldPropagateError_whenDatasourceFails() {
        when(datasource.findAll()).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> controller.searchInventory());
        verify(datasource, times(1)).findAll();
    }

    @Test
    void createInventoryItem_shouldReturnCreatedItem() {
        var createDto = new CreateInventoryItemDTO(
            "Alface",
            1,
            new BigDecimal("5.00"),
            "Comprar no mercado local"
        );

        var id = UUID.randomUUID();
        var now = LocalDateTime.now();
        var returnedDto = new GetInventoryDTO(id, "Alface", unitDTO, BigDecimal.ZERO, new BigDecimal("5.00"), "Comprar no mercado local", now, now);

        when(datasource.findUnitById(1)).thenReturn(java.util.Optional.of(unitDTO));
        when(datasource.create(any())).thenReturn(returnedDto);

        var result = controller.createInventoryItem(createDto);

        assertNotNull(result);
        assertEquals("Alface", result.name());
        assertEquals(unitDTO.name(), result.unit().name());

        verify(datasource, times(1)).findUnitById(1);
        verify(datasource, times(1)).create(any());
    }

    @Test
    void createInventoryItem_shouldThrowException_whenUnitNotFound() {
        var createDto = new CreateInventoryItemDTO("Tomate", 99, new BigDecimal("2.00"), "");

        when(datasource.findUnitById(99)).thenReturn(java.util.Optional.empty());

        assertThrows(RuntimeException.class, () -> controller.createInventoryItem(createDto));
        verify(datasource, never()).create(any());
    }
}
