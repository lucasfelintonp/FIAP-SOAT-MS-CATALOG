package br.com.fiap.fastfood.inventory.application.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "DTO de resposta contendo os dados de um item de inventário")
public record GetInventoryDTO(
    @Schema(
            description = "ID único do item de inventário",
            example = "550e8400-e29b-41d4-a716-446655440000"
    )
    UUID id,

    @Schema(
            description = "Nome do item de inventário",
            example = "Carne Bovina"
    )
    String name,

    @Schema(
            description = "Unidade de medida do item"
    )
    GetUnitDTO unit,

    @Schema(
            description = "Quantidade atual em estoque",
            example = "50.0"
    )
    BigDecimal quantity,

    @Schema(
            description = "Quantidade mínima em estoque",
            example = "10.0"
    )
    BigDecimal minimum_quantity,

    @Schema(
            description = "Observações sobre o item",
            example = "Armazenar em temperatura de 0-4°C"
    )
    String notes,

    @Schema(
            description = "Data e hora de criação do item",
            example = "2026-01-08T10:30:00"
    )
    LocalDateTime created_at,

    @Schema(
            description = "Data e hora da última atualização do item",
            example = "2026-01-08T15:45:00"
    )
    LocalDateTime updated_at
) {
}
