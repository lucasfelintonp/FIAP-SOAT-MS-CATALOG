package br.com.fiap.fastfood.inventory.application.controllers;

import br.com.fiap.fastfood.inventory.application.dtos.*;
import br.com.fiap.fastfood.inventory.infrastructure.interfaces.InventoryDatasource;
import br.com.fiap.fastfood.inventory.infrastructure.interfaces.InventoryProductsDatasource;
import br.com.fiap.fastfood.product.infrastructure.interfaces.ProductDatasource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryProductControllerTest {

    @Mock
    private InventoryProductsDatasource inventoryProductsDatasource;
    @Mock
    private InventoryDatasource inventoryDatasource;
    @Mock
    private ProductDatasource productDatasource;

    private InventoryProductController controller;

    @BeforeEach
    void setUp() {
        controller = new InventoryProductController(
            inventoryProductsDatasource,
            inventoryDatasource,
            productDatasource
        );
    }

    @Test
    void discountInventoryItemsByProducts_shouldExecuteSuccessfully() {
        var productId = UUID.randomUUID();
        var dtos = List.of(new ProductsQuantityDTO(productId, new BigDecimal("2.00")));

        when(inventoryProductsDatasource.getInventoryProductByProductId(any(UUID.class)))
            .thenReturn(List.of());

        controller.discountInventoryItemsByProducts(dtos);

        verify(inventoryProductsDatasource, atLeastOnce()).getInventoryProductByProductId(any(UUID.class));
    }

    @Test
    void createInventoryEntry_shouldExecuteFlowAndEnableProducts() {
        var inventoryId = UUID.randomUUID();
        var entryDto = new CreateInventoryEntryDTO(
            inventoryId,
            new BigDecimal("10.00"),
            LocalDate.now().plusDays(5),
            LocalDate.now()
        );

        var inventoryDto = new GetInventoryDTO(
            inventoryId, "Item", new GetUnitDTO(1, "UN", "UN"),
            BigDecimal.ZERO, BigDecimal.ONE, "", LocalDateTime.now(), LocalDateTime.now()
        );

        var entryResultDto = new GetInventoryEntryDTO(
            UUID.randomUUID(), inventoryDto, new BigDecimal("10.00"),
            entryDto.expirationDate(), entryDto.entryDate()
        );

        when(inventoryDatasource.getById(inventoryId)).thenReturn(inventoryDto);
        when(inventoryDatasource.createInventoryEntry(any())).thenReturn(entryResultDto);

        when(inventoryDatasource.getInventoryProductByInventoryId(inventoryId)).thenReturn(List.of());

        controller.createInventoryEntry(entryDto);

        verify(inventoryDatasource, times(1)).createInventoryEntry(any());
        verify(inventoryDatasource, atLeastOnce()).getInventoryProductByInventoryId(inventoryId);
    }

    @Test
    void createInventoryEntry_shouldPropagateException_whenInventoryNotFound() {
        var entryDto = new CreateInventoryEntryDTO(UUID.randomUUID(), BigDecimal.ONE, LocalDate.now(), LocalDate.now());

        when(inventoryDatasource.getById(any())).thenThrow(new RuntimeException("Inventory not found"));

        assertThrows(RuntimeException.class, () -> controller.createInventoryEntry(entryDto));
        verify(inventoryDatasource, never()).createInventoryEntry(any());
    }
}
