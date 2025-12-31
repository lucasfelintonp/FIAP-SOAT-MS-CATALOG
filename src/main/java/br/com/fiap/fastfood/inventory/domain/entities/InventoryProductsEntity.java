package br.com.fiap.fastfood.inventory.domain.entities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;

public class InventoryProductsEntity {

    private UUID id;
    private UUID productId;
    private InventoryEntity inventory;
    private BigDecimal quantity;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public InventoryProductsEntity(UUID id, UUID productId, InventoryEntity inventory, BigDecimal quantity, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = (id != null) ? id : UUID.randomUUID();

        if (productId == null) {
            throw new IllegalArgumentException("O ID do produto não pode ser nulo para um Produto do Estoque.");
        }

        if (inventory == null) {
            throw new IllegalArgumentException("O ID do item não pode ser nulo para um Produto do Estoque.");
        }

        if (quantity == null) {
            throw new IllegalArgumentException("A quantidade não pode ser nula para um Produto do Estoque.");
        }

        quantity = quantity.setScale(2, RoundingMode.HALF_UP);
        if (quantity.precision() > 5 || quantity.scale() > 2) {
            throw new IllegalArgumentException("A precisão da quantidade não pode exceder 5 dígitos no total, com 2 casas decimais.");
        }
        this.productId = productId;
        this.inventory = inventory;
        this.quantity = quantity;
        this.createdAt = (createdAt != null) ? createdAt : LocalDateTime.now();
        this.updatedAt = (updatedAt != null) ? updatedAt : LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getProductId() {
        return productId;
    }

    public InventoryEntity getInventory() {
        return inventory;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("O ID não pode ser nulo.");
        }
        this.id = id;
        this.updatedAt = LocalDateTime.now();
    }

    public void setProductId(UUID productId) {
        if (productId == null) {
            throw new IllegalArgumentException("O ID do produto não pode ser nulo.");
        }
        this.productId = productId;
        this.updatedAt = LocalDateTime.now();
    }

    public void setInventoryId(InventoryEntity inventory) {
        if (inventory == null) {
            throw new IllegalArgumentException("O item não pode ser nulo.");
        }
        this.inventory = inventory;
        this.updatedAt = LocalDateTime.now();
    }

    public void setQuantity(BigDecimal quantity) {
        if (quantity == null) {
            throw new IllegalArgumentException("A quantidade não pode ser nula.");
        }
        quantity = quantity.setScale(2, RoundingMode.HALF_UP);
        if (quantity.precision() > 5 || quantity.scale() > 2) {
            throw new IllegalArgumentException("A precisão da quantidade não pode exceder 5 dígitos no total, com 2 casas decimais.");
        }
        this.quantity = quantity;
        this.updatedAt = LocalDateTime.now();
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        if (updatedAt == null) {
            throw new IllegalArgumentException("A data de atualização não pode ser nula.");
        }
        this.updatedAt = updatedAt;
    }
}
