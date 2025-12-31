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

            validateInputQuantity(dto.quantity());

            List<InventoryProductsEntity> ingredients = gateway.getInventoryProductByProductId(dto.productId());

            for (InventoryProductsEntity ingredientRelation : ingredients) {
                InventoryEntity inventory = ingredientRelation.getInventory();

                BigDecimal currentQuantity = validateCurrentQuantity(inventory);

                BigDecimal totalToSubtract = dto.quantity().multiply(ingredientRelation.getQuantity());
                BigDecimal newQuantity = currentQuantity.subtract(totalToSubtract);

                if (newQuantity.compareTo(BigDecimal.ZERO) < 0) {
                    throw new InvalidQuantityException("Estoque insuficiente para o item: " + inventory.getName());
                }

                if (newQuantity.compareTo(BigDecimal.ZERO) == 0) {
                    disableDependentProducts(inventory.getId());
                }

                inventory.setQuantity(newQuantity);
                inventory.setUpdatedAt(LocalDateTime.now());
                inventoryGateway.update(inventory);
            }
        }
    }

    private void validateInputQuantity(BigDecimal quantity) {
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidQuantityException("A quantidade a ser descontada deve ser positiva.");
        }
    }

    private BigDecimal validateCurrentQuantity(InventoryEntity inventory) {
        if (inventory.getQuantity() == null) {
            throw new InvalidQuantityException("Quantidade atual nula para o item: " + inventory.getName());
        }
        return inventory.getQuantity();
    }

    private void disableDependentProducts(java.util.UUID inventoryId) {
        gateway.getInventoryProductByInventoryId(inventoryId)
            .forEach(relation -> productGateway.disableProduct(relation.getProductId()));
    }
}
