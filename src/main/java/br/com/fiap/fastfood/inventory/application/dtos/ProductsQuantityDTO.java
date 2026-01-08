package br.com.fiap.fastfood.inventory.application.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "DTO para desconto de itens do inventário baseado em produtos vendidos")
public record ProductsQuantityDTO(
    @Schema(
            description = "ID do produto vendido",
            example = "550e8400-e29b-41d4-a716-446655440000"
    )
    @NotNull(message = "ID do produto é obrigatório")
    UUID productId,

    @Schema(
            description = "Quantidade de produtos vendidos (será descontada do estoque)",
            example = "5.0",
            minimum = "0.01"
    )
    @NotNull(message = "Quantidade é obrigatória")
    @DecimalMin(value = "0.01", message = "Quantidade deve ser maior que zero")
    BigDecimal quantity
) {
}
