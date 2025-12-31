package br.com.fiap.fastfood.inventory.domain.use_cases;

import br.com.fiap.fastfood.inventory.application.dtos.CreateInventoryItemDTO;
import br.com.fiap.fastfood.inventory.application.gateways.InventoryGateway;
import br.com.fiap.fastfood.inventory.common.exceptions.EntityNotFoundException;
import br.com.fiap.fastfood.inventory.domain.entities.InventoryEntity;
import br.com.fiap.fastfood.inventory.domain.entities.UnitEntity;

import java.math.BigDecimal;

public class CreateInventoryItemUseCase {

    InventoryGateway gateway;

    public CreateInventoryItemUseCase(InventoryGateway gateway) {
        this.gateway = gateway;
    }

    public InventoryEntity run(CreateInventoryItemDTO dto) {

        UnitEntity unit = gateway.findUnitById(dto.unitId())
                .orElseThrow(() -> new EntityNotFoundException("Unidade não encontrada com o ID: " + dto.unitId()));

        InventoryEntity inventoryEntity = new InventoryEntity(
                null,
                dto.name(),
                unit,
                BigDecimal.ZERO,
                dto.minimumQuantity(),
                dto.notes(),
                null,
                null
        );

        return gateway.create(inventoryEntity);
    }
}
