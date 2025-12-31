package br.com.fiap.fastfood.inventory.domain.entities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.UUID;

public class InventoryEntryEntity {
    private UUID id;
    private InventoryEntity inventory;
    private BigDecimal quantity;
    private final LocalDate expirationDate;
    private final LocalDate entryDate;

    public InventoryEntryEntity(UUID id, InventoryEntity inventory, BigDecimal quantity, LocalDate expirationDate, LocalDate entryDate) {

        this.id = (id != null) ? id : UUID.randomUUID();

        if (inventory == null) throw new IllegalArgumentException("O item não pode ser nulo.");
        if (quantity == null) throw new IllegalArgumentException("A quantidade não pode ser nula.");
        if (entryDate == null) throw new IllegalArgumentException("A Data de Entrada não pode ser nula.");

        BigDecimal formattedQuantity = quantity.setScale(2, RoundingMode.HALF_UP);

        if (formattedQuantity.precision() > 5) {
            throw new IllegalArgumentException("A precisão da quantidade não pode exceder 5 dígitos no total.");
        }

        this.inventory = inventory;
        this.quantity = formattedQuantity;
        this.expirationDate = expirationDate;
        this.entryDate = entryDate;
    }

    public UUID getId() {
        return id;
    }

    public InventoryEntity getInventory() {
        return inventory;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("O ID não pode ser nulo.");
        }
        this.id = id;
    }

    public void setInventory(InventoryEntity inventory) {
        if (inventory == null) {
            throw new IllegalArgumentException("O item não pode ser nulo para uma Entrada de Estoque.");
        }
        this.inventory = inventory;
    }

    public void setQuantity(BigDecimal quantity) {
        if (quantity == null) {
            throw new IllegalArgumentException("A quantidade não pode ser nula para uma Entrada de Estoque.");
        }

        BigDecimal formattedQuantity = quantity.setScale(2, RoundingMode.HALF_UP);

        if (formattedQuantity.precision() > 5) {
            throw new IllegalArgumentException("A precisão da quantidade não pode exceder 5 dígitos no total.");
        }

        this.quantity = formattedQuantity;
    }
}
