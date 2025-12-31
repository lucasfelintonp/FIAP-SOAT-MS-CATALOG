package br.com.fiap.fastfood.inventory.application.gateways;

import br.com.fiap.fastfood.inventory.application.dtos.GetInventoryDTO;
import br.com.fiap.fastfood.inventory.application.dtos.GetUnitDTO;
import br.com.fiap.fastfood.inventory.domain.entities.InventoryEntity;
import br.com.fiap.fastfood.inventory.domain.entities.UnitEntity;
import br.com.fiap.fastfood.inventory.infrastructure.interfaces.InventoryDatasource;

import java.util.List;
import java.util.Optional;

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
