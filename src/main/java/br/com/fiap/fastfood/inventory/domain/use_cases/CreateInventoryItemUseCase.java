package br.com.fiap.fastfood.inventory.domain.use_cases;

import br.com.fiap.fastfood.inventory.application.dtos.CreateInventoryItemDTO;
import br.com.fiap.fastfood.inventory.application.gateways.InventoryGateway;
import br.com.fiap.fastfood.inventory.domain.entities.InventoryEntity;
import br.com.fiap.fastfood.inventory.domain.entities.UnitEntity;

public class CreateInventoryItemUseCase {

    InventoryGateway gateway;

    public CreateInventoryItemUseCase(InventoryGateway gateway) {
        this.gateway = gateway;
    }

    public InventoryEntity run(CreateInventoryItemDTO dto) {

        UnitEntity unit;
        try {
            unit = gateway.findUnitById(dto.unitId())
                .orElseThrow(()
                    -> new IllegalArgumentException("A unidade com o ID '" + dto.unitId() + "' não foi encontrada.")
                );
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao verificar a existência da unidade com ID '" + dto.unitId() + "': " + e.getMessage(), e);
        }

        InventoryEntity inventoryEntity = new InventoryEntity(
            null,
            dto.name(),
            unit,
            null,
            dto.minimumQuantity(),
            dto.notes(),
            null,
            null
        );

        return gateway.create(inventoryEntity);

    }
}
