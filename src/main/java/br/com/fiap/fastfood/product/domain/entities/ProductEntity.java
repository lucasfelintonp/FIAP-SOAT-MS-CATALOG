package br.com.fiap.fastfood.product.domain.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public record ProductEntity(
    UUID id,
    String name,
    String description,
    BigDecimal price,
    Boolean isActive,
    String imagePath,
    Integer categoryId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Optional<LocalDateTime> deletedAt
) {

    public Boolean getActive() {
        return isActive;
    }
}

