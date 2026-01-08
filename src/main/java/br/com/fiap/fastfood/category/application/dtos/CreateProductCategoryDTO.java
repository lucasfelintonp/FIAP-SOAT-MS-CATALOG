package br.com.fiap.fastfood.category.application.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para criação de categoria de produto")
public record CreateProductCategoryDTO(
        @Schema(
                description = "Nome da categoria",
                example = "Bebidas",
                minLength = 3,
                maxLength = 50
        )
        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 3, max = 50, message = "Nome deve ter entre 3 e 50 caracteres")
        String name
) {
}
