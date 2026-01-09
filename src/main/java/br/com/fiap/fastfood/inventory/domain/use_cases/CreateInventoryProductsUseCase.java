package br.com.fiap.fastfood.inventory.domain.use_cases;

import br.com.fiap.fastfood.inventory.application.gateways.InventoryGateway;
import br.com.fiap.fastfood.inventory.domain.entities.InventoryEntity;
import br.com.fiap.fastfood.inventory.domain.entities.InventoryProductsEntity;
import br.com.fiap.fastfood.product.application.dtos.CreateProductDTO;
import br.com.fiap.fastfood.product.domain.entities.ProductEntity;

import java.time.LocalDateTime;

public class CreateInventoryProductsUseCase {
    InventoryGateway gateway;

    public CreateInventoryProductsUseCase(InventoryGateway inventoryGateway) {
        this.gateway = inventoryGateway;
    }

    public void run(ProductEntity productEntity, CreateProductDTO dto) {
        if (dto.inventories() == null || dto.inventories().isEmpty()) {
            return;
        }

        dto.inventories().forEach(inventory -> {
            InventoryEntity inventoryEntity = gateway.getById(inventory.inventoryId());

            InventoryProductsEntity entity = new InventoryProductsEntity(
                null,
                productEntity.id(),
                inventoryEntity,
                inventory.quantity(),
                LocalDateTime.now(),
                LocalDateTime.now()
            );

            gateway.createInventoryProduct(entity);
        });

    }
}
