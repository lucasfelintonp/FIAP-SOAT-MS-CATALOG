package br.com.fiap.fastfood.inventory.domain.use_cases;

import br.com.fiap.fastfood.inventory.application.dtos.CreateInventoryItemDTO;
import br.com.fiap.fastfood.inventory.application.gateways.InventoryGateway;
import br.com.fiap.fastfood.inventory.common.exceptions.EntityNotFoundException;
import br.com.fiap.fastfood.inventory.domain.entities.InventoryEntity;
import br.com.fiap.fastfood.inventory.domain.entities.UnitEntity;

import java.math.BigDecimal;

/**
 * Use case for creating inventory items.
 */
public class CreateInventoryItemUseCase {

    private final InventoryGateway gateway;

    /**
     * Constructor.
     *
     * @param gateway the inventory gateway
     */
    public CreateInventoryItemUseCase(final InventoryGateway gateway) {
        this.gateway = gateway;
    }

    /**
     * Executes the use case.
     *
     * @param dto the inventory item creation data
     * @return the created inventory entity
     */
    public InventoryEntity run(final CreateInventoryItemDTO dto) {

        UnitEntity unit = gateway.findUnitById(dto.unitId())
                .orElseThrow(() -> new EntityNotFoundException(
                    "Unidade não encontrada com o ID: " + dto.unitId()
                ));

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
