package br.com.fiap.fastfood.inventory.application.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record GetInventoryProductDTO(
    UUID id,
    UUID productId,
    GetInventoryDTO inventory,
    BigDecimal quantity,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
