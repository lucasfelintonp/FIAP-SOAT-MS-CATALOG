package br.com.fiap.fastfood.inventory.common.handlers;

import br.com.fiap.fastfood.inventory.application.dtos.*;
import br.com.fiap.fastfood.inventory.infrastructure.interfaces.InventoryDatasource;
import br.com.fiap.fastfood.inventory.infrastructure.interfaces.InventoryProductsDatasource;
import br.com.fiap.fastfood.product.infrastructure.interfaces.ProductDatasource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class InventoryHandlerTest {

    private InventoryDatasource inventoryDatasource;
    private InventoryProductsDatasource inventoryProductsDatasource;
    private ProductDatasource productDatasource;
    private InventoryHandler handler;
    private GetUnitDTO unitDTO;

    @BeforeEach
    void setUp() {
        inventoryDatasource = mock(InventoryDatasource.class);
        inventoryProductsDatasource = mock(InventoryProductsDatasource.class);
        productDatasource = mock(ProductDatasource.class);

        handler = new InventoryHandler(
            inventoryDatasource,
            inventoryProductsDatasource,
            productDatasource
        );

        unitDTO = new GetUnitDTO(1, "Quilograma", "KG");
    }

    @Test
    void searchInventoryReturnsListAnd200() {
        GetInventoryDTO item = new GetInventoryDTO(
            UUID.randomUUID(),
            "Carne",
            unitDTO,
            new BigDecimal("100.00"),
            new BigDecimal("10.00"),
            "Nota fiscal",
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        when(inventoryDatasource.findAll()).thenReturn(List.of(item));

        var response = handler.searchInventory();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        verify(inventoryDatasource, times(1)).findAll();
    }

    @Test
    void createInventoryItemReturnsCreatedAnd201() {
        CreateInventoryItemDTO inputDto = new CreateInventoryItemDTO(
            "Pão", 1, new BigDecimal("50.00"), "Fornecedor X"
        );

        GetInventoryDTO createdDto = new GetInventoryDTO(
            UUID.randomUUID(),
            "Pão",
            unitDTO,
            BigDecimal.ZERO,
            new BigDecimal("50.00"),
            "Fornecedor X",
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        doReturn(java.util.Optional.of(unitDTO))
            .when(inventoryDatasource).findUnitById(anyInt());

        when(inventoryDatasource.create(any(GetInventoryDTO.class))).thenReturn(createdDto);

        var response = handler.createInventoryItem(inputDto);

        assertEquals(201, response.getStatusCode().value());
        assertEquals("Pão", response.getBody().name());
        assertNotNull(response.getBody().unit());
    }

    @Test
    void discountInventoryItemsByProductsReturns200() {
        List<ProductsQuantityDTO> dtos = List.of(
            new ProductsQuantityDTO(UUID.randomUUID(), new BigDecimal("2.00"))
        );

        var response = handler.discountInventoryItemsByProducts(dtos);

        assertEquals(200, response.getStatusCode().value());
        verify(inventoryProductsDatasource, atLeastOnce()).getInventoryProductByProductId(any());
    }

    @Test
    void createInventoryEntryReturns201() {
        UUID inventoryId = UUID.randomUUID();
        CreateInventoryEntryDTO inputDto = new CreateInventoryEntryDTO(
            inventoryId,
            new BigDecimal("10.00"),
            LocalDate.now().plusDays(10),
            LocalDate.now()
        );

        GetInventoryDTO inventoryDTO = new GetInventoryDTO(
            inventoryId, "Carne", unitDTO, new BigDecimal("50.00"),
            new BigDecimal("10.00"), "", LocalDateTime.now(), LocalDateTime.now()
        );

        when(inventoryDatasource.getById(inventoryId)).thenReturn(inventoryDTO);

        GetInventoryEntryDTO mockResult = new GetInventoryEntryDTO(
            UUID.randomUUID(),
            inventoryDTO,
            new BigDecimal("10.00"),
            LocalDate.now().plusDays(10),
            LocalDate.now()
        );

        when(inventoryDatasource.createInventoryEntry(any(CreateInventoryEntryDTO.class)))
            .thenReturn(mockResult);

        var response = handler.createInventoryEntry(inputDto);

        assertEquals(201, response.getStatusCode().value());
        verify(inventoryDatasource, times(1)).createInventoryEntry(any(CreateInventoryEntryDTO.class));
    }
}
