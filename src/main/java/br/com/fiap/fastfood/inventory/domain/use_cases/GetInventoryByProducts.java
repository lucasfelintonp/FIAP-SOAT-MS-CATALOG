package br.com.fiap.fastfood.inventory.domain.use_cases;

import br.com.fiap.fastfood.inventory.application.dtos.ProductsQuantityDTO;
import br.com.fiap.fastfood.inventory.application.gateways.InventoryGateway;
import br.com.fiap.fastfood.inventory.application.gateways.InventoryProductGateway;
import br.com.fiap.fastfood.inventory.common.exceptions.InvalidQuantityException;
import br.com.fiap.fastfood.inventory.domain.entities.InventoryEntity;
import br.com.fiap.fastfood.inventory.domain.entities.InventoryProductsEntity;
import br.com.fiap.fastfood.product.application.gateways.ProductGateway;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class GetInventoryByProducts {

    InventoryProductGateway gateway;
    InventoryGateway inventoryGateway;
    ProductGateway productGateway;

    public GetInventoryByProducts(
        InventoryProductGateway gateway,
        InventoryGateway inventoryGateway,
        ProductGateway productGateway
    ) {
        this.gateway = gateway;
        this.inventoryGateway = inventoryGateway;
        this.productGateway = productGateway;

    }

    public void run(List<ProductsQuantityDTO> dtos) {

        for (ProductsQuantityDTO dto : dtos) {
            List<InventoryProductsEntity> inventories = gateway.getInventoryProductByProductId(dto.productId());

            inventories.forEach(inventoryProduct -> {
                InventoryEntity inventory = inventoryGateway.getById(inventoryProduct.getInventory().getId());

                if (dto.quantity().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new InvalidQuantityException("A quantidade a ser descontada deve ser positiva.");
                }

                BigDecimal currentQuantity = inventory.getQuantity();

                if (currentQuantity == null) {
                    throw new InvalidQuantityException("Quantidade atual do item " + inventoryProduct.getInventory().getId() + " é nula.");
                }

                BigDecimal quantityToSubtract = dto.quantity().multiply(inventoryProduct.getQuantity());
                BigDecimal newQuantity = currentQuantity.subtract(quantityToSubtract);

                if (newQuantity.compareTo(BigDecimal.ZERO) < 0) {
                    throw new InvalidQuantityException("Estoque insuficiente para concluir o pedido.");
                }

                if (newQuantity.compareTo(BigDecimal.ZERO) == 0) {

                    List<InventoryProductsEntity> inventoryProductRelatedEntities = gateway.getInventoryProductByInventoryId(inventoryProduct.getInventory().getId());

                    for (InventoryProductsEntity relation : inventoryProductRelatedEntities) {
                        UUID productId = relation.getProductId();

                        productGateway.disableProduct(productId);
                    }
                }

                inventory.setQuantity(newQuantity);
                inventory.setUpdatedAt(LocalDateTime.now());

                inventoryGateway.update(inventory);
            });


        }
    }
}
