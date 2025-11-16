package br.com.fiap.fastfood.product.application.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductDTO(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        Boolean isActive,
        String imagePath,
        Integer categoryId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
