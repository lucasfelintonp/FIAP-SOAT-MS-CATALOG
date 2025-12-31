package br.com.fiap.fastfood.inventory.domain.use_cases;

import br.com.fiap.fastfood.inventory.application.dtos.CreateInventoryEntryDTO;
import br.com.fiap.fastfood.inventory.application.gateways.InventoryGateway;
import br.com.fiap.fastfood.inventory.domain.entities.InventoryEntity;
import br.com.fiap.fastfood.inventory.domain.entities.InventoryEntryEntity;

import java.math.BigDecimal;

public class CreateInventoryEntryUseCase {

    InventoryGateway gateway;

    public CreateInventoryEntryUseCase(
        InventoryGateway gateway
    ) {
        this.gateway = gateway;
    }

    public InventoryEntryEntity run(CreateInventoryEntryDTO dto) {

        InventoryEntity inventory = gateway.getById(dto.inventoryId());

        BigDecimal quantity;
        quantity = dto.quantity().add(inventory.getQuantity());
        inventory.setQuantity(quantity);

        InventoryEntryEntity inventoryEntry = new InventoryEntryEntity(
            null,
            inventory,
            dto.quantity(),
            dto.expirationDate(),
            dto.entryDate()
        );

        InventoryEntryEntity persistedInventoryEntry = gateway.createInventoryEntry(inventoryEntry);

        gateway.update(inventory);

        return persistedInventoryEntry;

    }

}
