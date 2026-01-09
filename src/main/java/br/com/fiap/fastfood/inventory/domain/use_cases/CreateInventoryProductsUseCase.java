package br.com.fiap.fastfood.inventory.domain.use_cases;

import br.com.fiap.fastfood.inventory.application.gateways.InventoryGateway;
import br.com.fiap.fastfood.inventory.domain.entities.InventoryEntity;
import br.com.fiap.fastfood.inventory.domain.entities.InventoryProductsEntity;
import br.com.fiap.fastfood.product.application.dtos.CreateProductDTO;
import br.com.fiap.fastfood.product.domain.entities.ProductEntity;

import java.time.LocalDateTime;

/**
 * Use case for creating inventory products.
 */
public class CreateInventoryProductsUseCase {
    private final InventoryGateway gateway;

    /**
     * Constructor.
     *
     * @param inventoryGateway the inventory gateway
     */
    public CreateInventoryProductsUseCase(final InventoryGateway inventoryGateway) {
        this.gateway = inventoryGateway;
    }

    /**
     * Executes the use case.
     *
     * @param productEntity the product entity
     * @param dto the product creation data
     */
    public void run(final ProductEntity productEntity, final CreateProductDTO dto) {
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
