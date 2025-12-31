package br.com.fiap.fastfood.inventory.application.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductsQuantityDTO(
    UUID productId,
    BigDecimal quantity
) {
}
