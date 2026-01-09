package br.com.fiap.fastfood.inventory.domain.use_cases;

import br.com.fiap.fastfood.inventory.application.dtos.CreateInventoryEntryDTO;
import br.com.fiap.fastfood.inventory.application.gateways.InventoryGateway;
import br.com.fiap.fastfood.inventory.domain.entities.InventoryEntity;
import br.com.fiap.fastfood.inventory.domain.entities.InventoryEntryEntity;

/**
 * Use case for creating inventory entries.
 */
public class CreateInventoryEntryUseCase {

    private final InventoryGateway gateway;

    /**
     * Constructor.
     *
     * @param gateway the inventory gateway
     */
    public CreateInventoryEntryUseCase(final InventoryGateway gateway) {
        this.gateway = gateway;
    }

    /**
     * Executes the use case.
     *
     * @param dto the inventory entry creation data
     * @return the created inventory entry entity
     */
    public InventoryEntryEntity run(final CreateInventoryEntryDTO dto) {

        InventoryEntity inventory = gateway.getById(dto.inventoryId());

        inventory.setQuantity(inventory.getQuantity().add(dto.quantity()));

        InventoryEntryEntity inventoryEntry = new InventoryEntryEntity(
                null,
                inventory,
                dto.quantity(),
                dto.expirationDate(),
                dto.entryDate()
        );

        gateway.update(inventory);

        return gateway.createInventoryEntry(inventoryEntry);
    }
}
