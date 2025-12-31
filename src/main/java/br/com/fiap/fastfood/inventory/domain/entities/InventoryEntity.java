package br.com.fiap.fastfood.inventory.domain.entities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;

public class InventoryEntity {
    private final UUID id;
    private final String name;
    private final UnitEntity unit;
    private BigDecimal quantity;
    private final BigDecimal minimumQuantity;
    private final String notes;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public InventoryEntity(UUID id, String name, UnitEntity unit, BigDecimal quantity, BigDecimal minimumQuantity, String notes, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = (id != null) ? id : UUID.randomUUID();

        validateName(name);
        validateUnit(unit);
        validateQuantity(quantity);
        validateMinimumQuantity(minimumQuantity);
        validateNotes(notes);

        this.name = name.trim();
        this.unit = unit;
        this.quantity = (quantity != null) ? quantity.setScale(2, RoundingMode.HALF_UP) : null;
        this.minimumQuantity = minimumQuantity.setScale(2, RoundingMode.HALF_UP);
        this.notes = (notes != null) ? notes.trim() : null;
        this.createdAt = (createdAt != null) ? createdAt : LocalDateTime.now();
        this.updatedAt = (updatedAt != null) ? updatedAt : LocalDateTime.now();
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do item não pode ser nulo ou vazio.");
        }
        if (name.length() > 50) {
            throw new IllegalArgumentException("O nome do item não pode ter mais de 50 caracteres.");
        }
    }

    private void validateUnit(UnitEntity unit) {
        if (unit == null) {
            throw new IllegalArgumentException("A unidade não pode ser nula para um Estoque.");
        }
    }

    private void validateQuantity(BigDecimal quantity) {
        if (quantity != null) {
            BigDecimal formatted = quantity.setScale(2, RoundingMode.HALF_UP);
            if (formatted.precision() > 5) {
                throw new IllegalArgumentException("A precisão da quantidade não pode exceder 5 dígitos no total.");
            }
        }
    }

    private void validateMinimumQuantity(BigDecimal minimumQuantity) {
        if (minimumQuantity == null) {
            throw new IllegalArgumentException("A quantidade mínima não pode ser nula para um Estoque.");
        }
        if (minimumQuantity.precision() > 5 || minimumQuantity.scale() > 2) {
            throw new IllegalArgumentException("A precisão da quantidade mínima não pode exceder 5 dígitos no total, com 2 casas decimais.");
        }
    }

    private void validateNotes(String notes) {
        if (notes != null && notes.length() > 100) {
            throw new IllegalArgumentException("As notas não podem ter mais de 100 caracteres.");
        }
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public UnitEntity getUnit() {
        return unit;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public BigDecimal getMinimumQuantity() {
        return minimumQuantity;
    }

    public String getNotes() {
        return notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setQuantity(BigDecimal quantity) {
        validateQuantity(quantity);
        this.quantity = (quantity != null) ? quantity.setScale(2, RoundingMode.HALF_UP) : null;
        this.updatedAt = LocalDateTime.now();
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        if (updatedAt == null) {
            throw new IllegalArgumentException("A data de atualização não pode ser nula.");
        }
        this.updatedAt = updatedAt;
    }
}
