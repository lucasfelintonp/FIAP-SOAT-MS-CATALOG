package br.com.fiap.fastfood.inventory.application.dtos;

import java.math.BigDecimal;

public record CreateInventoryItemDTO(
    String name,
    Integer unitId,
    BigDecimal minimumQuantity,
    String notes
) {
}
