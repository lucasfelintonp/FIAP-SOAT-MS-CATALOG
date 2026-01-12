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
        // Arrange
        GetInventoryDTO item1 = new GetInventoryDTO(
            UUID.randomUUID(),
            "Carne",
            unitDTO,
            new BigDecimal("100.00"),
            new BigDecimal("10.00"),
            "Nota fiscal",
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        GetInventoryDTO item2 = new GetInventoryDTO(
            UUID.randomUUID(),
            "Pão",
            unitDTO,
            BigDecimal.valueOf(20.00),
            BigDecimal.valueOf(5.00),
            "Fornecedor X",
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        when(inventoryDatasource.findAll()).thenReturn(List.of(item1, item2));

        // Act
        var response = handler.searchInventory();

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(inventoryDatasource, times(1)).findAll();
    }

    @Test
    void createInventoryItemReturnsCreatedAnd201() {
        // Arrange
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

        // Act
        var response = handler.createInventoryItem(inputDto);

        // Assert
        assertEquals(201, response.getStatusCode().value());
        assertEquals("Pão", response.getBody().name());
        assertNotNull(response.getBody().unit());
        verify(inventoryDatasource, times(1)).create(any());
    }

    @Test
    void createInventoryItemWithDifferentData_shouldReturn201() {
        // Arrange
        CreateInventoryItemDTO dto = new CreateInventoryItemDTO(
            "Tomato",
            1,
            BigDecimal.valueOf(5.00),
            "Fresh tomatoes"
        );

        GetInventoryDTO expectedInventory = new GetInventoryDTO(
            UUID.randomUUID(),
            "Tomato",
            unitDTO,
            BigDecimal.ZERO,
            BigDecimal.valueOf(5.00),
            "Fresh tomatoes",
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        when(inventoryDatasource.findUnitById(1)).thenReturn(java.util.Optional.of(unitDTO));
        when(inventoryDatasource.create(any())).thenReturn(expectedInventory);

        // Act
        var response = handler.createInventoryItem(dto);

        // Assert
        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Tomato", response.getBody().name());
        assertEquals(unitDTO, response.getBody().unit());
        // Use compareTo for BigDecimal comparison (scale-independent)
        assertEquals(0, response.getBody().minimum_quantity().compareTo(new BigDecimal("5.00")));
        verify(inventoryDatasource, times(1)).create(any());
    }

    @Test
    void discountInventoryItemsByProductsReturns200() {
        // Arrange
        List<ProductsQuantityDTO> dtos = List.of(
            new ProductsQuantityDTO(UUID.randomUUID(), new BigDecimal("2.00"))
        );

        // Act
        var response = handler.discountInventoryItemsByProducts(dtos);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        verify(inventoryProductsDatasource, atLeastOnce()).getInventoryProductByProductId(any());
    }

    @Test
    void createInventoryEntryReturns201() {
        // Arrange
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

        // Act
        var response = handler.createInventoryEntry(inputDto);

        // Assert
        assertEquals(201, response.getStatusCode().value());
        verify(inventoryDatasource, times(1)).createInventoryEntry(any(CreateInventoryEntryDTO.class));
    }

    @Test
    void createInventoryEntryWithDifferentExpiration_shouldReturn201() {
        // Arrange
        UUID inventoryId = UUID.randomUUID();
        CreateInventoryEntryDTO dto = new CreateInventoryEntryDTO(
            inventoryId,
            BigDecimal.valueOf(10.00),
            LocalDate.now().plusDays(30),
            LocalDate.now()
        );

        GetInventoryDTO inventoryDTO = new GetInventoryDTO(
            inventoryId, "Item", unitDTO, new BigDecimal("50.00"),
            new BigDecimal("10.00"), "", LocalDateTime.now(), LocalDateTime.now()
        );

        when(inventoryDatasource.getById(inventoryId)).thenReturn(inventoryDTO);

        GetInventoryEntryDTO mockResult = new GetInventoryEntryDTO(
            UUID.randomUUID(),
            inventoryDTO,
            BigDecimal.valueOf(10.00),
            LocalDate.now().plusDays(30),
            LocalDate.now()
        );

        when(inventoryDatasource.createInventoryEntry(any(CreateInventoryEntryDTO.class)))
            .thenReturn(mockResult);

        // Act
        var response = handler.createInventoryEntry(dto);

        // Assert
        assertEquals(201, response.getStatusCode().value());
        assertNotNull(mockResult.inventory());
        // Use compareTo for BigDecimal comparison (scale-independent)
        assertEquals(0, mockResult.quantity().compareTo(new BigDecimal("10.00")));
        verify(inventoryDatasource, times(1)).createInventoryEntry(any(CreateInventoryEntryDTO.class));
    }
}
