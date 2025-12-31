package br.com.fiap.fastfood.inventory.infrastructure.interfaces;

import br.com.fiap.fastfood.inventory.application.dtos.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InventoryDatasource {

    List<GetInventoryDTO> findAll();

    Optional<GetUnitDTO> findUnitById(Integer unitId);

    GetInventoryDTO create(GetInventoryDTO dto);

    GetInventoryDTO getById(UUID id);

    void update(GetInventoryDTO dto);

    GetInventoryEntryDTO createInventoryEntry(CreateInventoryEntryDTO dto);

    List<GetInventoryProductDTO> getInventoryProductByInventoryId(UUID inventoryId);

    List<GetInventoryProductDTO> getInventoryProductByProductId(UUID productId);
}
