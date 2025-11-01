package br.com.fiap.fastfood.products.application.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateProductDTO(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        Boolean isActive,
        String imagePath,
        Integer categoryId
) {
}
