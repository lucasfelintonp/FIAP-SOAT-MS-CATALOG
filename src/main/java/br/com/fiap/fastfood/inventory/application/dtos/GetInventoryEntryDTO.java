package br.com.fiap.fastfood.inventory.application.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "DTO de resposta contendo os dados de uma entrada/lote de estoque")
public record GetInventoryEntryDTO(
    @Schema(
            description = "ID único da entrada de estoque",
            example = "550e8400-e29b-41d4-a716-446655440000"
    )
    UUID id,

    @Schema(
            description = "Dados do item de inventário"
    )
    GetInventoryDTO inventory,

    @Schema(
            description = "Quantidade adicionada ao estoque",
            example = "100.0"
    )
    BigDecimal quantity,

    @Schema(
            description = "Data de validade do lote",
            example = "2026-06-30"
    )
    LocalDate expirationDate,

    @Schema(
            description = "Data de entrada do lote",
            example = "2026-01-08"
    )
    LocalDate entryDate
) {
}
