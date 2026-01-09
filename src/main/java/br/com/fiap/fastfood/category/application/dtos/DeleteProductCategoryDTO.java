package br.com.fiap.fastfood.category.application.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de resposta quando uma categoria é excluída")
public record DeleteProductCategoryDTO(
        @Schema(
                description = "ID da categoria excluída",
                example = "1"
        )
        Integer id
) {
}
