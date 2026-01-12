package br.com.fiap.fastfood.inventory.application.controllers;

import br.com.fiap.fastfood.inventory.application.dtos.*;
import br.com.fiap.fastfood.inventory.common.exceptions.InvalidQuantityException;
import br.com.fiap.fastfood.inventory.infrastructure.interfaces.InventoryDatasource;
import br.com.fiap.fastfood.inventory.infrastructure.interfaces.InventoryProductsDatasource;
import br.com.fiap.fastfood.product.infrastructure.interfaces.ProductDatasource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
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
    @DisplayName("Should execute discount successfully with valid products and quantities")
    void discountInventoryItemsByProducts_shouldExecuteSuccessfully() {
        var productId = UUID.randomUUID();
        var dtos = List.of(new ProductsQuantityDTO(productId, new BigDecimal("2.00")));

        when(inventoryProductsDatasource.getInventoryProductByProductId(any(UUID.class)))
            .thenReturn(List.of());

        controller.discountInventoryItemsByProducts(dtos);

        verify(inventoryProductsDatasource, atLeastOnce()).getInventoryProductByProductId(any(UUID.class));
    }

    @Test
    @DisplayName("Should handle null list in discount by creating defensive copy")
    void discountInventoryItemsByProducts_shouldHandleNullList() {
        assertDoesNotThrow(() -> controller.discountInventoryItemsByProducts(null));
    }

    @Test
    @DisplayName("Should handle empty list in discount without errors")
    void discountInventoryItemsByProducts_shouldHandleEmptyList() {
        assertDoesNotThrow(() -> controller.discountInventoryItemsByProducts(Collections.emptyList()));
        verify(inventoryProductsDatasource, never()).getInventoryProductByProductId(any());
    }

    @Test
    @DisplayName("Should propagate exception when invalid quantity is provided")
    void discountInventoryItemsByProducts_shouldPropagateException_whenInvalidQuantity() {
        var productId = UUID.randomUUID();
        var dtos = List.of(new ProductsQuantityDTO(productId, BigDecimal.ZERO));

        assertThrows(InvalidQuantityException.class,
            () -> controller.discountInventoryItemsByProducts(dtos));
    }

    @Test
    @DisplayName("Should propagate exception when negative quantity is provided")
    void discountInventoryItemsByProducts_shouldPropagateException_whenNegativeQuantity() {
        var productId = UUID.randomUUID();
        var dtos = List.of(new ProductsQuantityDTO(productId, new BigDecimal("-1.00")));

        assertThrows(InvalidQuantityException.class,
            () -> controller.discountInventoryItemsByProducts(dtos));
    }

    @Test
    @DisplayName("Should process multiple products in discount operation")
    void discountInventoryItemsByProducts_shouldHandleMultipleProducts() {
        var productId1 = UUID.randomUUID();
        var productId2 = UUID.randomUUID();
        var dtos = List.of(
            new ProductsQuantityDTO(productId1, new BigDecimal("1.00")),
            new ProductsQuantityDTO(productId2, new BigDecimal("3.00"))
        );

        when(inventoryProductsDatasource.getInventoryProductByProductId(any(UUID.class)))
            .thenReturn(List.of());

        controller.discountInventoryItemsByProducts(dtos);

        verify(inventoryProductsDatasource, times(2)).getInventoryProductByProductId(any(UUID.class));
    }

    @Test
    @DisplayName("Should propagate exception when inventory datasource fails during discount")
    void discountInventoryItemsByProducts_shouldPropagateException_whenDatasourceFails() {
        var productId = UUID.randomUUID();
        var dtos = List.of(new ProductsQuantityDTO(productId, new BigDecimal("2.00")));

        when(inventoryProductsDatasource.getInventoryProductByProductId(any(UUID.class)))
            .thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class,
            () -> controller.discountInventoryItemsByProducts(dtos));
    }

    @Test
    @DisplayName("Should create inventory entry and enable eligible products successfully")
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
    @DisplayName("Should propagate exception when inventory not found during entry creation")
    void createInventoryEntry_shouldPropagateException_whenInventoryNotFound() {
        var entryDto = new CreateInventoryEntryDTO(UUID.randomUUID(), BigDecimal.ONE, LocalDate.now(), LocalDate.now());

        when(inventoryDatasource.getById(any())).thenThrow(new RuntimeException("Inventory not found"));

        assertThrows(RuntimeException.class, () -> controller.createInventoryEntry(entryDto));
        verify(inventoryDatasource, never()).createInventoryEntry(any());
    }

    @Test
    @DisplayName("Should create entry with large quantity value within limit")
    void createInventoryEntry_shouldHandleLargeQuantity() {
        var inventoryId = UUID.randomUUID();
        var largeQuantity = new BigDecimal("999.99"); // Within 5-digit limit
        var entryDto = new CreateInventoryEntryDTO(
            inventoryId,
            largeQuantity,
            LocalDate.now().plusDays(30),
            LocalDate.now()
        );

        var inventoryDto = new GetInventoryDTO(
            inventoryId, "Item", new GetUnitDTO(1, "UN", "UN"),
            BigDecimal.ZERO, BigDecimal.ONE, "", LocalDateTime.now(), LocalDateTime.now()
        );

        var entryResultDto = new GetInventoryEntryDTO(
            UUID.randomUUID(), inventoryDto, largeQuantity,
            entryDto.expirationDate(), entryDto.entryDate()
        );

        when(inventoryDatasource.getById(inventoryId)).thenReturn(inventoryDto);
        when(inventoryDatasource.createInventoryEntry(any())).thenReturn(entryResultDto);
        when(inventoryDatasource.getInventoryProductByInventoryId(inventoryId)).thenReturn(List.of());

        assertDoesNotThrow(() -> controller.createInventoryEntry(entryDto));
        verify(inventoryDatasource, times(1)).createInventoryEntry(any());
    }

    @Test
    @DisplayName("Should propagate exception when quantity exceeds precision limit")
    void createInventoryEntry_shouldPropagateException_whenQuantityExceedsPrecision() {
        var inventoryId = UUID.randomUUID();
        var invalidQuantity = new BigDecimal("999999.99"); // Exceeds 5-digit limit
        var entryDto = new CreateInventoryEntryDTO(
            inventoryId,
            invalidQuantity,
            LocalDate.now().plusDays(30),
            LocalDate.now()
        );

        var inventoryDto = new GetInventoryDTO(
            inventoryId, "Item", new GetUnitDTO(1, "UN", "UN"),
            BigDecimal.ZERO, BigDecimal.ONE, "", LocalDateTime.now(), LocalDateTime.now()
        );

        when(inventoryDatasource.getById(inventoryId)).thenReturn(inventoryDto);

        assertThrows(IllegalArgumentException.class, () -> controller.createInventoryEntry(entryDto));
        verify(inventoryDatasource, never()).createInventoryEntry(any());
    }

    @Test
    @DisplayName("Should create entry with expiration date in the past")
    void createInventoryEntry_shouldHandlePastExpirationDate() {
        var inventoryId = UUID.randomUUID();
        var entryDto = new CreateInventoryEntryDTO(
            inventoryId,
            new BigDecimal("5.00"),
            LocalDate.now().minusDays(1), // Past date
            LocalDate.now()
        );

        var inventoryDto = new GetInventoryDTO(
            inventoryId, "Item", new GetUnitDTO(1, "UN", "UN"),
            BigDecimal.ZERO, BigDecimal.ONE, "", LocalDateTime.now(), LocalDateTime.now()
        );

        var entryResultDto = new GetInventoryEntryDTO(
            UUID.randomUUID(), inventoryDto, new BigDecimal("5.00"),
            entryDto.expirationDate(), entryDto.entryDate()
        );

        when(inventoryDatasource.getById(inventoryId)).thenReturn(inventoryDto);
        when(inventoryDatasource.createInventoryEntry(any())).thenReturn(entryResultDto);
        when(inventoryDatasource.getInventoryProductByInventoryId(inventoryId)).thenReturn(List.of());

        assertDoesNotThrow(() -> controller.createInventoryEntry(entryDto));
        verify(inventoryDatasource, times(1)).createInventoryEntry(any());
    }

    @Test
    @DisplayName("Should verify update is called during entry creation")
    void createInventoryEntry_shouldCallUpdateDuringCreation() {
        var inventoryId = UUID.randomUUID();
        var entryDto = new CreateInventoryEntryDTO(
            inventoryId,
            new BigDecimal("15.00"),
            LocalDate.now().plusDays(10),
            LocalDate.now()
        );

        var inventoryDto = new GetInventoryDTO(
            inventoryId, "Ingredient", new GetUnitDTO(2, "KG", "Kilogram"),
            new BigDecimal("5.00"), new BigDecimal("2.00"), "Ref123",
            LocalDateTime.now(), LocalDateTime.now()
        );

        var entryResultDto = new GetInventoryEntryDTO(
            UUID.randomUUID(), inventoryDto, new BigDecimal("15.00"),
            entryDto.expirationDate(), entryDto.entryDate()
        );

        when(inventoryDatasource.getById(inventoryId)).thenReturn(inventoryDto);
        when(inventoryDatasource.createInventoryEntry(any())).thenReturn(entryResultDto);
        when(inventoryDatasource.getInventoryProductByInventoryId(inventoryId)).thenReturn(List.of());

        controller.createInventoryEntry(entryDto);

        verify(inventoryDatasource, times(1)).update(any());
        verify(inventoryDatasource, times(1)).createInventoryEntry(any());
    }

    @Test
    @DisplayName("Should enable products when inventory products exist")
    void createInventoryEntry_shouldEnableProducts_whenInventoryProductsExist() {
        var inventoryId = UUID.randomUUID();
        var productId = UUID.randomUUID();
        var entryDto = new CreateInventoryEntryDTO(
            inventoryId,
            new BigDecimal("20.00"),
            LocalDate.now().plusDays(15),
            LocalDate.now()
        );

        var inventoryDto = new GetInventoryDTO(
            inventoryId, "Item", new GetUnitDTO(1, "UN", "UN"),
            new BigDecimal("5.00"), new BigDecimal("3.00"), "",
            LocalDateTime.now(), LocalDateTime.now()
        );

        var entryResultDto = new GetInventoryEntryDTO(
            UUID.randomUUID(), inventoryDto, new BigDecimal("20.00"),
            entryDto.expirationDate(), entryDto.entryDate()
        );

        var inventoryProductDto = new GetInventoryProductDTO(
            UUID.randomUUID(), productId, inventoryDto,
            new BigDecimal("2.00"), LocalDateTime.now(), LocalDateTime.now()
        );

        when(inventoryDatasource.getById(inventoryId)).thenReturn(inventoryDto);
        when(inventoryDatasource.createInventoryEntry(any())).thenReturn(entryResultDto);
        when(inventoryDatasource.getInventoryProductByInventoryId(inventoryId))
            .thenReturn(List.of(inventoryProductDto));
        when(inventoryDatasource.getInventoryProductByProductId(productId))
            .thenReturn(List.of(inventoryProductDto));

        controller.createInventoryEntry(entryDto);

        verify(inventoryDatasource, times(1)).createInventoryEntry(any());
        verify(inventoryDatasource, atLeastOnce()).getInventoryProductByInventoryId(inventoryId);
        verify(inventoryDatasource, atLeastOnce()).getInventoryProductByProductId(productId);
        verify(productDatasource, atLeastOnce()).enable(productId);
    }

    @Test
    @DisplayName("Should propagate exception when entry creation fails")
    void createInventoryEntry_shouldPropagateException_whenCreationFails() {
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

        when(inventoryDatasource.getById(inventoryId)).thenReturn(inventoryDto);
        when(inventoryDatasource.createInventoryEntry(any()))
            .thenThrow(new RuntimeException("Failed to create entry"));

        assertThrows(RuntimeException.class, () -> controller.createInventoryEntry(entryDto));
        verify(inventoryDatasource, times(1)).createInventoryEntry(any());
    }
}
