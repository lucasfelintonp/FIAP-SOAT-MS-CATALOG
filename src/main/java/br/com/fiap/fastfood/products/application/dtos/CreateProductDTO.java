package br.com.fiap.fastfood.products.application.dtos;

import java.math.BigDecimal;

public record CreateProductDTO(
        String name,
        String description,
        BigDecimal price,
        Boolean isActive,
        String imagePath,
        Integer categoryId
        // TODO: Inventory List
) {
}
