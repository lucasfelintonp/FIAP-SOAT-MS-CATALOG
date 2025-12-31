package br.com.fiap.fastfood.inventory.domain.use_cases;

import br.com.fiap.fastfood.inventory.application.gateways.InventoryGateway;
import br.com.fiap.fastfood.inventory.domain.entities.InventoryEntryEntity;
import br.com.fiap.fastfood.inventory.domain.entities.InventoryProductsEntity;
import br.com.fiap.fastfood.product.application.gateways.ProductGateway;

import java.util.List;

public class EnableElegibleProductsUseCase {

    InventoryGateway inventoryGateway;
    ProductGateway productGateway;

    public EnableElegibleProductsUseCase(
        InventoryGateway inventoryGateway,
        ProductGateway productGateway
    ) {
        this.inventoryGateway = inventoryGateway;
        this.productGateway = productGateway;
    }

    public void run(InventoryEntryEntity entity) {
        List<InventoryProductsEntity> relatedProducts = inventoryGateway.getInventoryProductByInventoryId(entity.getInventory().getId());

        for (InventoryProductsEntity product : relatedProducts) {
            List<InventoryProductsEntity> inventories = inventoryGateway.getInventoryProductByProductId(product.getProductId());

            boolean allItemsInStock = inventories.stream()
                .map(inventory -> inventoryGateway.getById(inventory.getInventory().getId()))
                .allMatch(inv -> inv.getQuantity().compareTo(inv.getMinimumQuantity()) > 0);

            if (allItemsInStock) {
                productGateway.enableProduct(product.getProductId());
            }


        }

    }

}
