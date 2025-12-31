package br.com.fiap.fastfood.inventory.application.gateways;

import br.com.fiap.fastfood.inventory.application.dtos.CreateInventoryEntryDTO;
import br.com.fiap.fastfood.inventory.application.dtos.GetInventoryDTO;
import br.com.fiap.fastfood.inventory.application.dtos.GetInventoryProductDTO;
import br.com.fiap.fastfood.inventory.application.dtos.GetUnitDTO;
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

        var result = datasource.findAll();

        return result.stream()
            .map(this::dtoToInventoryEntity
            ).toList();
    }

    public Optional<UnitEntity> findUnitById(Integer unitId) {
        var result = datasource.findUnitById(unitId);

        if (result.isPresent()) {
            UnitEntity unitEntity = new UnitEntity(
                result.get().id(),
                result.get().name(),
                result.get().abbreviation()
            );

            return Optional.of(unitEntity);
        }

        return Optional.empty();

    }

    public InventoryEntity create(InventoryEntity inventory) {

        var result = datasource.create(
            new GetInventoryDTO(
                null,
                inventory.getName(),
                new GetUnitDTO(
                    inventory.getUnit().getId(),
                    inventory.getUnit().getName(),
                    inventory.getUnit().getAbbreviation()
                ),
                inventory.getQuantity(),
                inventory.getMinimumQuantity(),
                inventory.getNotes(),
                inventory.getCreatedAt(),
                inventory.getUpdatedAt()
            )
        );

        return dtoToInventoryEntity(result);
    }

    public InventoryEntity getById(UUID id) {

        var result = datasource.getById(id);

        return new InventoryEntity(
            result.id(),
            result.name(),
            new UnitEntity(result.unit().id(), result.unit().name(), result.unit().abbreviation()),
            result.quantity(),
            result.minimum_quantity(),
            result.notes(),
            result.created_at(),
            result.updated_at()
        );

    }

    public void update(InventoryEntity inventory) {
        datasource.update(new GetInventoryDTO(
            inventory.getId(),
            inventory.getName(),
            new GetUnitDTO(inventory.getUnit().getId(), inventory.getUnit().getName(), inventory.getUnit().getAbbreviation()),
            inventory.getQuantity(),
            inventory.getMinimumQuantity(),
            inventory.getNotes(),
            inventory.getCreatedAt(),
            inventory.getUpdatedAt()
        ));
    }

    public InventoryEntryEntity createInventoryEntry(InventoryEntryEntity inventoryEntry) {

        var result = datasource.createInventoryEntry(new CreateInventoryEntryDTO(
            inventoryEntry.getInventory().getId(),
            inventoryEntry.getQuantity(),
            inventoryEntry.getEntryDate(),
            inventoryEntry.getExpirationDate()
        ));

        return new InventoryEntryEntity(
            result.id(),
            new InventoryEntity(
                result.inventory().id(),
                result.inventory().name(),
                new UnitEntity(
                    result.inventory().unit().id(),
                    result.inventory().unit().name(),
                    result.inventory().unit().abbreviation()
                ),
                result.inventory().quantity(),
                result.inventory().minimum_quantity(),
                result.inventory().notes(),
                result.inventory().created_at(),
                result.inventory().updated_at()
            ),
            result.quantity(),
            result.expirationDate(),
            result.entryDate()
        );

    }

    public List<InventoryProductsEntity> getInventoryProductByInventoryId(UUID inventoryId) {
        List<GetInventoryProductDTO> result = datasource.getInventoryProductByInventoryId(inventoryId);

        return result.stream()
            .map(dto -> new InventoryProductsEntity(
                    dto.id(),
                    dto.productId(),
                    new InventoryEntity(
                        dto.inventory().id(),
                        dto.inventory().name(),
                        new UnitEntity(
                            dto.inventory().unit().id(),
                            dto.inventory().unit().name(),
                            dto.inventory().unit().abbreviation()
                        ),
                        dto.inventory().quantity(),
                        dto.inventory().minimum_quantity(),
                        dto.inventory().notes(),
                        dto.inventory().created_at(),
                        dto.inventory().updated_at()
                    ),
                    dto.quantity(),
                    dto.createdAt(),
                    dto.updatedAt()
                )
            ).toList();
    }

    public List<InventoryProductsEntity> getInventoryProductByProductId(UUID productId) {
        List<GetInventoryProductDTO> result = datasource.getInventoryProductByProductId(productId);

        return result.stream()
            .map(dto -> new InventoryProductsEntity(
                    dto.id(),
                    dto.productId(),
                    new InventoryEntity(
                        dto.inventory().id(),
                        dto.inventory().name(),
                        new UnitEntity(
                            dto.inventory().unit().id(),
                            dto.inventory().unit().name(),
                            dto.inventory().unit().abbreviation()
                        ),
                        dto.inventory().quantity(),
                        dto.inventory().minimum_quantity(),
                        dto.inventory().notes(),
                        dto.inventory().created_at(),
                        dto.inventory().updated_at()
                    ),
                    dto.quantity(),
                    dto.createdAt(),
                    dto.updatedAt()
                )
            ).toList();
    }

    private InventoryEntity dtoToInventoryEntity(GetInventoryDTO dto) {

        return new InventoryEntity(dto.id(),
            dto.name(),
            new UnitEntity(
                dto.unit().id(),
                dto.unit().name(),
                dto.unit().abbreviation()
            ),
            dto.quantity(),
            dto.minimum_quantity(),
            dto.notes(),
            dto.created_at(),
            dto.updated_at()
        );

    }
}
