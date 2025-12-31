package br.com.fiap.fastfood.inventory.application.gateways;

import br.com.fiap.fastfood.inventory.application.dtos.*;
import br.com.fiap.fastfood.inventory.domain.entities.InventoryEntity;
import br.com.fiap.fastfood.inventory.domain.entities.InventoryEntryEntity;
import br.com.fiap.fastfood.inventory.domain.entities.UnitEntity;
import br.com.fiap.fastfood.inventory.infrastructure.interfaces.InventoryDatasource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryGatewayTest {

    @Mock
    private InventoryDatasource datasource;

    private InventoryGateway gateway;

    private GetUnitDTO unitDTO;
    private GetInventoryDTO inventoryDTO;

    @BeforeEach
    void setUp() {
        gateway = new InventoryGateway(datasource);
        unitDTO = new GetUnitDTO(1, "Quilograma", "KG");
        inventoryDTO = new GetInventoryDTO(
            UUID.randomUUID(), "Carne", unitDTO, new BigDecimal("10.00"),
            new BigDecimal("2.00"), "Nota A", LocalDateTime.now(), LocalDateTime.now()
        );
    }

    @Test
    void findAll_shouldMapToEntityList() {
        when(datasource.findAll()).thenReturn(List.of(inventoryDTO));

        List<InventoryEntity> result = gateway.findAll();

        assertFalse(result.isEmpty());
        assertEquals(inventoryDTO.name(), result.get(0).getName());
        assertEquals("KG", result.get(0).getUnit().getAbbreviation());
        verify(datasource, times(1)).findAll();
    }

    @Test
    void findUnitById_shouldReturnMappedOptional() {
        when(datasource.findUnitById(1)).thenReturn(Optional.of(unitDTO));

        Optional<UnitEntity> result = gateway.findUnitById(1);

        assertTrue(result.isPresent());
        assertEquals("Quilograma", result.get().getName());
    }

    @Test
    void create_shouldSendDTOAndReturnEntity() {
        InventoryEntity entityToCreate = new InventoryEntity(
            null, "Pão", new UnitEntity(1, "Un", "UN"),
            BigDecimal.ZERO, BigDecimal.TEN, "", null, null
        );

        when(datasource.create(any(GetInventoryDTO.class))).thenReturn(inventoryDTO);

        InventoryEntity result = gateway.create(entityToCreate);

        assertNotNull(result);
        verify(datasource, times(1)).create(any());
    }

    @Test
    void getById_shouldReturnMappedEntity() {
        UUID id = inventoryDTO.id();
        when(datasource.getById(id)).thenReturn(inventoryDTO);

        InventoryEntity result = gateway.getById(id);

        assertEquals(id, result.getId());
        assertEquals("Carne", result.getName());
    }

    @Test
    void createInventoryEntry_shouldMapCorrectData() {
        InventoryEntity invEntity = new InventoryEntity(UUID.randomUUID(), "Item",
            new UnitEntity(1, "U", "U"), BigDecimal.ZERO, BigDecimal.ZERO, "", null, null);

        InventoryEntryEntity entryEntity = new InventoryEntryEntity(
            null, invEntity, new BigDecimal("50.00"), LocalDate.now(), LocalDate.now().plusDays(10)
        );

        GetInventoryEntryDTO resultDto = new GetInventoryEntryDTO(
            UUID.randomUUID(), inventoryDTO, new BigDecimal("50.00"),
            LocalDate.now(), LocalDate.now().plusDays(10)
        );

        when(datasource.createInventoryEntry(any(CreateInventoryEntryDTO.class))).thenReturn(resultDto);

        InventoryEntryEntity result = gateway.createInventoryEntry(entryEntity);

        assertNotNull(result.getId());
        assertEquals(new BigDecimal("50.00"), result.getQuantity());
        verify(datasource, times(1)).createInventoryEntry(any());
    }

    @Test
    void getInventoryProductByProductId_shouldReturnList() {
        UUID productId = UUID.randomUUID();
        GetInventoryProductDTO ipDto = new GetInventoryProductDTO(
            UUID.randomUUID(), productId, inventoryDTO, BigDecimal.ONE, LocalDateTime.now(), LocalDateTime.now()
        );

        when(datasource.getInventoryProductByProductId(productId)).thenReturn(List.of(ipDto));

        var result = gateway.getInventoryProductByProductId(productId);

        assertEquals(1, result.size());
        assertEquals(productId, result.get(0).getProductId());
    }
}
