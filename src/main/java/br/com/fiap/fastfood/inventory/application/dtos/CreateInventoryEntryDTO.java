package br.com.fiap.fastfood.inventory.application.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateInventoryEntryDTO(
    UUID inventoryId,
    BigDecimal quantity,
    LocalDate entryDate,
    LocalDate expirationDate
) {
}
