package br.com.fiap.fastfood.inventory.infrastructure.database.adapters;

import br.com.fiap.fastfood.inventory.application.dtos.CreateInventoryEntryDTO;
import br.com.fiap.fastfood.inventory.application.dtos.GetInventoryDTO;
import br.com.fiap.fastfood.inventory.application.dtos.GetUnitDTO;
import br.com.fiap.fastfood.inventory.common.exceptions.EntityNotFoundException;
import br.com.fiap.fastfood.inventory.infrastructure.database.entities.InventoryEntityJPA;
import br.com.fiap.fastfood.inventory.infrastructure.database.entities.InventoryEntryEntityJPA;
import br.com.fiap.fastfood.inventory.infrastructure.database.entities.InventoryProductsEntityJPA;
import br.com.fiap.fastfood.inventory.infrastructure.database.entities.UnitEntityJPA;
import br.com.fiap.fastfood.inventory.infrastructure.database.repositories.InventoryEntryRepository;
import br.com.fiap.fastfood.inventory.infrastructure.database.repositories.InventoryProductsRepository;
import br.com.fiap.fastfood.inventory.infrastructure.database.repositories.InventoryRepository;
import br.com.fiap.fastfood.inventory.infrastructure.database.repositories.UnitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryAdapterTest {

    @Mock
    private InventoryRepository inventoryRepository;
    @Mock
    private UnitRepository unitRepository;
    @Mock
    private InventoryEntryRepository inventoryEntryRepository;
    @Mock
    private InventoryProductsRepository inventoryProductsRepository;

    @InjectMocks
    private InventoryAdapter adapter;

    private UUID inventoryId;
    private UnitEntityJPA unitKg;
    private InventoryEntityJPA inventoryEntity;
    private GetInventoryDTO inventoryDTO;

    @BeforeEach
    void setUp() {
        inventoryId = UUID.randomUUID();

        unitKg = new UnitEntityJPA();
        unitKg.setId(1);
        unitKg.setName("Quilograma");
        unitKg.setAbbreviation("KG");

        inventoryEntity = new InventoryEntityJPA(
            inventoryId,
            "Carne Moída",
            unitKg,
            new BigDecimal("10.0"),
            new BigDecimal("2.0"),
            "Carne de primeira",
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        inventoryDTO = new GetInventoryDTO(
            inventoryId,
            "Carne Moída",
            new GetUnitDTO(1, "Quilograma", "KG"),
            new BigDecimal("10.0"),
            new BigDecimal("2.0"),
            "Carne de primeira",
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    @Test
    void findAll_success_shouldReturnList() {
        when(inventoryRepository.findAll()).thenReturn(List.of(inventoryEntity));

        var result = adapter.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(0, new BigDecimal("10.0").compareTo(result.get(0).quantity()));
        verify(inventoryRepository).findAll();
    }

    @Test
    void create_success_shouldCreateInventory() {
        when(unitRepository.findById(1)).thenReturn(Optional.of(unitKg));
        when(inventoryRepository.save(any(InventoryEntityJPA.class))).thenReturn(inventoryEntity);

        var result = adapter.create(inventoryDTO);

        assertNotNull(result);
        assertEquals("Carne Moída", result.name());
        assertEquals(0, new BigDecimal("10.0").compareTo(result.quantity()));
        verify(inventoryRepository).save(any(InventoryEntityJPA.class));
    }

    @Test
    void update_success_shouldUpdateExistingItem() {
        when(inventoryRepository.findById(inventoryId)).thenReturn(Optional.of(inventoryEntity));

        GetInventoryDTO updateDTO = new GetInventoryDTO(
            inventoryId,
            "Carne Moída de segunda",
            new GetUnitDTO(1, "Quilograma", "KG"),
            new BigDecimal("15.5"),
            new BigDecimal("5.0"),
            "Carne de segunda",
            null,
            LocalDateTime.now()
        );

        adapter.update(updateDTO);

        verify(inventoryRepository).save(any(InventoryEntityJPA.class));
        assertEquals("Carne Moída de segunda", inventoryEntity.getName());
    }

    @Test
    void createInventoryEntry_success_shouldSaveEntry() {
        CreateInventoryEntryDTO entryDTO = new CreateInventoryEntryDTO(
            inventoryId,
            new BigDecimal("5.0"),
            null,
            null
        );

        InventoryEntryEntityJPA entryEntity = new InventoryEntryEntityJPA(
            UUID.randomUUID(),
            inventoryEntity,
            new BigDecimal("5.0"),
            null,
            null
        );

        when(inventoryRepository.findById(inventoryId)).thenReturn(Optional.of(inventoryEntity));
        when(inventoryEntryRepository.save(any(InventoryEntryEntityJPA.class))).thenReturn(entryEntity);

        var result = adapter.createInventoryEntry(entryDTO);

        assertNotNull(result);
        assertEquals(0, new BigDecimal("5.0").compareTo(result.quantity()));
    }

    @Test
    void getInventoryProductByInventoryId_success_shouldReturnList() {
        InventoryProductsEntityJPA ipEntity = new InventoryProductsEntityJPA();
        ipEntity.setInventory(inventoryEntity);
        ipEntity.setQuantity(new BigDecimal("10.0"));

        when(inventoryProductsRepository.findByInventoryId(inventoryId)).thenReturn(List.of(ipEntity));

        var result = adapter.getInventoryProductByInventoryId(inventoryId);

        assertNotNull(result);
        assertEquals(0, new BigDecimal("10.0").compareTo(result.get(0).quantity()));
    }
}
