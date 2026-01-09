package br.com.fiap.fastfood.inventory.application.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de resposta contendo os dados de uma unidade de medida")
public record GetUnitDTO(
    @Schema(
            description = "ID único da unidade",
            example = "1"
    )
    Integer id,

    @Schema(
            description = "Nome da unidade de medida",
            example = "Quilograma"
    )
    String name,

    @Schema(
            description = "Abreviação da unidade",
            example = "kg"
    )
    String abbreviation
) {
}
