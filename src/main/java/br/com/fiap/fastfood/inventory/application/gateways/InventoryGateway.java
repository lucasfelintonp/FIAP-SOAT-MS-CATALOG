package br.com.fiap.fastfood.inventory.application.gateways;

import br.com.fiap.fastfood.inventory.application.dtos.*;
import br.com.fiap.fastfood.inventory.domain.entities.InventoryEntity;
import br.com.fiap.fastfood.inventory.domain.entities.InventoryEntryEntity;
import br.com.fiap.fastfood.inventory.domain.entities.InventoryProductsEntity;
import br.com.fiap.fastfood.inventory.domain.entities.UnitEntity;
import br.com.fiap.fastfood.inventory.infrastructure.interfaces.InventoryDatasource;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class InventoryGateway {

    private final InventoryDatasource datasource;

    public InventoryGateway(InventoryDatasource datasource) {
        this.datasource = datasource;
    }

    public List<InventoryEntity> findAll() {
        return datasource.findAll().stream()
            .map(this::mapToInventoryEntity)
            .toList();
    }

    public Optional<UnitEntity> findUnitById(Integer unitId) {
        return datasource.findUnitById(unitId)
            .map(dto -> new UnitEntity(dto.id(), dto.name(), dto.abbreviation()));
    }

    public InventoryEntity create(InventoryEntity inventory) {
        var result = datasource.create(mapToGetInventoryDTO(inventory));
        return mapToInventoryEntity(result);
    }

    public InventoryEntity getById(UUID id) {
        return mapToInventoryEntity(datasource.getById(id));
    }

    public void update(InventoryEntity inventory) {
        datasource.update(mapToGetInventoryDTO(inventory));
    }

    public InventoryEntryEntity createInventoryEntry(InventoryEntryEntity entry) {
        var result = datasource.createInventoryEntry(new CreateInventoryEntryDTO(
            entry.getInventory().getId(),
            entry.getQuantity(),
            entry.getEntryDate(),
            entry.getExpirationDate()
        ));

        return new InventoryEntryEntity(
            result.id(),
            mapToInventoryEntity(result.inventory()),
            result.quantity(),
            result.expirationDate(),
            result.entryDate()
        );
    }

    public List<InventoryProductsEntity> getInventoryProductByInventoryId(UUID inventoryId) {
        return datasource.getInventoryProductByInventoryId(inventoryId).stream()
            .map(this::mapToInventoryProductsEntity)
            .toList();
    }

    public List<InventoryProductsEntity> getInventoryProductByProductId(UUID productId) {
        return datasource.getInventoryProductByProductId(productId).stream()
            .map(this::mapToInventoryProductsEntity)
            .toList();
    }

    public void createInventoryProduct(InventoryProductsEntity entity) {

        PersistInventoryProductDTO dto = new PersistInventoryProductDTO(
            null,
            entity.getProductId(),
            entity.getInventory().getId(),
            entity.getQuantity(),
            null,
            null
        );

        datasource.createInventoryProduct(dto);
    }

    private InventoryEntity mapToInventoryEntity(GetInventoryDTO dto) {
        return new InventoryEntity(
            dto.id(),
            dto.name(),
            new UnitEntity(dto.unit().id(), dto.unit().name(), dto.unit().abbreviation()),
            dto.quantity(),
            dto.minimum_quantity(),
            dto.notes(),
            dto.created_at(),
            dto.updated_at()
        );
    }

    private GetInventoryDTO mapToGetInventoryDTO(InventoryEntity entity) {
        return new GetInventoryDTO(
            entity.getId(),
            entity.getName(),
            new GetUnitDTO(entity.getUnit().getId(), entity.getUnit().getName(), entity.getUnit().getAbbreviation()),
            entity.getQuantity(),
            entity.getMinimumQuantity(),
            entity.getNotes(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    private InventoryProductsEntity mapToInventoryProductsEntity(GetInventoryProductDTO dto) {
        return new InventoryProductsEntity(
            dto.id(),
            dto.productId(),
            mapToInventoryEntity(dto.inventory()),
            dto.quantity(),
            dto.createdAt(),
            dto.updatedAt()
        );
    }
}
