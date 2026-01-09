package br.com.fiap.fastfood.product.application.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "DTO de resposta quando um produto é excluído")
public record DeletedProductDTO(
        @Schema(
                description = "ID do produto excluído",
                example = "550e8400-e29b-41d4-a716-446655440000"
        )
        UUID id
) {
}
