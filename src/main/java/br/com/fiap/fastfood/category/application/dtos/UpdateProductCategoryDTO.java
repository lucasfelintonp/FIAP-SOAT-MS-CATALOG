package br.com.fiap.fastfood.category.application.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para atualização de categoria de produto existente")
public record UpdateProductCategoryDTO(
        @Schema(
                description = "ID da categoria a ser atualizada",
                example = "1"
        )
        @NotNull(message = "ID é obrigatório")
        Integer id,

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
