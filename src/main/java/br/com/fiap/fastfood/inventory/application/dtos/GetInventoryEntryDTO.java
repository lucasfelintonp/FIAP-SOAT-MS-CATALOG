package br.com.fiap.fastfood.inventory.application.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record GetInventoryEntryDTO(
    UUID id,
    GetInventoryDTO inventory,
    BigDecimal quantity,
    LocalDate expirationDate,
    LocalDate entryDate
) {
}
