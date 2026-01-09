package br.com.fiap.fastfood.category.application.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de resposta contendo os dados de uma categoria de produto")
public record ProductCategoryDTO(
        @Schema(
                description = "ID único da categoria",
                example = "1"
        )
        Integer id,

        @Schema(
                description = "Nome da categoria",
                example = "Bebidas"
        )
        String name
) {
}
