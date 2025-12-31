package br.com.fiap.fastfood.inventory.application.gateways;


import br.com.fiap.fastfood.inventory.application.dtos.GetInventoryProductDTO;
import br.com.fiap.fastfood.inventory.domain.entities.InventoryEntity;
import br.com.fiap.fastfood.inventory.domain.entities.InventoryProductsEntity;
import br.com.fiap.fastfood.inventory.domain.entities.UnitEntity;
import br.com.fiap.fastfood.inventory.infrastructure.interfaces.InventoryProductsDatasource;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

@Slf4j
public class InventoryProductGateway {

    private final InventoryProductsDatasource datasource;

    public InventoryProductGateway(InventoryProductsDatasource datasource) {
        this.datasource = datasource;
    }

    public List<InventoryProductsEntity> getInventoryProductByProductId(UUID productId) {
        List<GetInventoryProductDTO> result = datasource.getInventoryProductByProductId(productId);

        return result.stream()
            .map(dto -> new InventoryProductsEntity(
                    dto.id(),
                    dto.productId(),
                    new InventoryEntity(
                        dto.inventory().id(),
                        dto.inventory().name(),
                        new UnitEntity(
                            dto.inventory().unit().id(),
                            dto.inventory().unit().name(),
                            dto.inventory().unit().abbreviation()
                        ),
                        dto.inventory().quantity(),
                        dto.inventory().minimum_quantity(),
                        dto.inventory().notes(),
                        dto.inventory().created_at(),
                        dto.inventory().updated_at()
                    ),
                    dto.quantity(),
                    dto.createdAt(),
                    dto.updatedAt()
                )
            ).toList();
    }

    public List<InventoryProductsEntity> getInventoryProductByInventoryId(UUID inventoryId) {
        List<GetInventoryProductDTO> result = datasource.getInventoryProductByInventoryId(inventoryId);

        return result.stream()
            .map(dto -> new InventoryProductsEntity(
                    dto.id(),
                    dto.productId(),
                    new InventoryEntity(
                        dto.inventory().id(),
                        dto.inventory().name(),
                        new UnitEntity(
                            dto.inventory().unit().id(),
                            dto.inventory().unit().name(),
                            dto.inventory().unit().abbreviation()
                        ),
                        dto.inventory().quantity(),
                        dto.inventory().minimum_quantity(),
                        dto.inventory().notes(),
                        dto.inventory().created_at(),
                        dto.inventory().updated_at()
                    ),
                    dto.quantity(),
                    dto.createdAt(),
                    dto.updatedAt()
                )
            ).toList();
    }

}
