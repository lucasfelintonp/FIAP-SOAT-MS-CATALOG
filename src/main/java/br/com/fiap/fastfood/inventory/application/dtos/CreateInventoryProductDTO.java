package br.com.fiap.fastfood.inventory.application.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "DTO para criação de relação entre produto e item de inventário")
public record CreateInventoryProductDTO(
    @Schema(
            description = "ID do item de inventário",
            example = "550e8400-e29b-41d4-a716-446655440000"
    )
    @NotNull(message = "ID do inventário é obrigatório")
    UUID inventoryId,

    @Schema(
            description = "Quantidade do item necessária para o produto",
            example = "0.5",
            minimum = "0.01"
    )
    @NotNull(message = "Quantidade é obrigatória")
    @DecimalMin(value = "0.01", message = "Quantidade deve ser maior que zero")
    BigDecimal quantity
) {
}
