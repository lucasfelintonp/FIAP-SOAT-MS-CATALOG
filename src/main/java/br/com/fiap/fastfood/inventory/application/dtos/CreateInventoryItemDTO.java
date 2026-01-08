package br.com.fiap.fastfood.inventory.application.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(description = "DTO para criação de novo item de inventário")
public record CreateInventoryItemDTO(
    @Schema(
            description = "Nome do item de inventário",
            example = "Carne Bovina",
            minLength = 3,
            maxLength = 100
    )
    @NotBlank(message = "Nome é obrigatório")
    String name,

    @Schema(
            description = "ID da unidade de medida",
            example = "1"
    )
    @NotNull(message = "Unidade é obrigatória")
    Integer unitId,

    @Schema(
            description = "Quantidade mínima do item em estoque",
            example = "10.0",
            minimum = "0"
    )
    @DecimalMin(value = "0", message = "Quantidade mínima deve ser maior ou igual a zero")
    BigDecimal minimumQuantity,

    @Schema(
            description = "Observações sobre o item de inventário",
            example = "Armazenar em temperatura de 0-4°C"
    )
    String notes
) {
}
