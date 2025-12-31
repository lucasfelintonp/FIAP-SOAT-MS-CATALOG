package br.com.fiap.fastfood.inventory.application.gateways;


import br.com.fiap.fastfood.inventory.application.dtos.GetInventoryDTO;
import br.com.fiap.fastfood.inventory.application.dtos.GetInventoryProductDTO;
import br.com.fiap.fastfood.inventory.domain.entities.InventoryEntity;
import br.com.fiap.fastfood.inventory.domain.entities.InventoryProductsEntity;
import br.com.fiap.fastfood.inventory.domain.entities.UnitEntity;
import br.com.fiap.fastfood.inventory.infrastructure.interfaces.InventoryProductsDatasource;

import java.util.List;
import java.util.UUID;

public class InventoryProductGateway {

    private final InventoryProductsDatasource datasource;

    public InventoryProductGateway(InventoryProductsDatasource datasource) {
        this.datasource = datasource;
    }

    public List<InventoryProductsEntity> getInventoryProductByProductId(UUID productId) {
        return datasource.getInventoryProductByProductId(productId).stream()
            .map(this::mapToInventoryProductsEntity)
            .toList();
    }

    public List<InventoryProductsEntity> getInventoryProductByInventoryId(UUID inventoryId) {
        return datasource.getInventoryProductByInventoryId(inventoryId).stream()
            .map(this::mapToInventoryProductsEntity)
            .toList();
    }

    private InventoryProductsEntity mapToInventoryProductsEntity(GetInventoryProductDTO dto) {
        return new InventoryProductsEntity(
            dto.id(),
            dto.productId(),
            mapToInventoryEntity(dto.inventory()),
            dto.quantity(),
            dto.createdAt(),
            dto.updatedAt()
        );
    }

    private InventoryEntity mapToInventoryEntity(GetInventoryDTO dto) {
        return new InventoryEntity(
            dto.id(),
            dto.name(),
            new UnitEntity(
                dto.unit().id(),
                dto.unit().name(),
                dto.unit().abbreviation()
            ),
            dto.quantity(),
            dto.minimum_quantity(),
            dto.notes(),
            dto.created_at(),
            dto.updated_at()
        );
    }
}
