package br.com.fiap.fastfood.product.application.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "DTO para atualização de produto existente")
public record UpdateProductDTO(
        @Schema(
                description = "ID do produto a ser atualizado",
                example = "550e8400-e29b-41d4-a716-446655440000"
        )
        @NotNull(message = "ID é obrigatório")
        UUID id,

        @Schema(
                description = "Nome do produto",
                example = "Hambúrguer Clássico",
                minLength = 3,
                maxLength = 100
        )
        @NotBlank(message = "Nome é obrigatório")
        String name,

        @Schema(
                description = "Descrição detalhada do produto",
                example = "Delicioso hambúrguer com queijo, alface e tomate"
        )
        String description,

        @Schema(
                description = "Preço do produto em reais",
                example = "25.90",
                minimum = "0.01",
                maximum = "9999.99"
        )
        @NotNull(message = "Preço é obrigatório")
        @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
        BigDecimal price,

        @Schema(
                description = "Indica se o produto está ativo no catálogo",
                example = "true"
        )
        Boolean isActive,

        @Schema(
                description = "Caminho da imagem do produto",
                example = "/images/produtos/hamburguer-classico.jpg"
        )
        String imagePath,

        @Schema(
                description = "ID da categoria à qual o produto pertence",
                example = "1"
        )
        @NotNull(message = "Categoria é obrigatória")
        Integer categoryId
) {
}
