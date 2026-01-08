package br.com.fiap.fastfood.inventory.application.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "DTO interno para persistência de relação entre produto e inventário")
public record PersistInventoryProductDTO(
    @Schema(
            description = "ID único do relacionamento",
            example = "550e8400-e29b-41d4-a716-446655440000"
    )
    UUID id,

    @Schema(
            description = "ID do produto",
            example = "650e8400-e29b-41d4-a716-446655440001"
    )
    UUID productId,

    @Schema(
            description = "ID do item de inventário",
            example = "750e8400-e29b-41d4-a716-446655440002"
    )
    UUID inventoryId,

    @Schema(
            description = "Quantidade do item necessária para o produto",
            example = "0.5"
    )
    BigDecimal quantity,

    @Schema(
            description = "Data e hora de criação do relacionamento",
            example = "2026-01-08T10:30:00"
    )
    LocalDateTime createdAt,

    @Schema(
            description = "Data e hora da última atualização do relacionamento",
            example = "2026-01-08T15:45:00"
    )
    LocalDateTime updatedAt

) {
}

