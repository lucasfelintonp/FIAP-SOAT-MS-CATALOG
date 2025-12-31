package br.com.fiap.fastfood.inventory.infrastructure.database.adapters;

import br.com.fiap.fastfood.inventory.application.dtos.GetInventoryDTO;
import br.com.fiap.fastfood.inventory.application.dtos.GetInventoryProductDTO;
import br.com.fiap.fastfood.inventory.application.dtos.GetUnitDTO;
import br.com.fiap.fastfood.inventory.infrastructure.database.entities.InventoryEntityJPA;
import br.com.fiap.fastfood.inventory.infrastructure.database.entities.InventoryProductsEntityJPA;
import br.com.fiap.fastfood.inventory.infrastructure.database.entities.UnitEntityJPA;
import br.com.fiap.fastfood.inventory.infrastructure.database.repositories.InventoryProductsRepository;
import br.com.fiap.fastfood.inventory.infrastructure.interfaces.InventoryProductsDatasource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class InventoryProductsAdapter implements InventoryProductsDatasource {

    private final InventoryProductsRepository repository;

    @Autowired
    public InventoryProductsAdapter(
        InventoryProductsRepository repository
    ){
        this.repository = repository;
    }

    @Override
    public List<GetInventoryProductDTO> getInventoryProductByProductId(UUID productId) {
        var result = repository.findByProductId(productId);

        return result.stream().map(this::inventoryProductEntityToDto).toList();
    }

    @Override
    public List<GetInventoryProductDTO> getInventoryProductByInventoryId(UUID inventoryId) {
        var result = repository.findByInventoryId(inventoryId);

        return result.stream().map(this::inventoryProductEntityToDto).toList();
    }

    private GetInventoryProductDTO inventoryProductEntityToDto(InventoryProductsEntityJPA entityJPA) {
        return new GetInventoryProductDTO(
            entityJPA.getId(),
            entityJPA.getProductId(),
            inventoryEntityToDto(entityJPA.getInventory()),
            entityJPA.getQuantity(),
            entityJPA.getCreatedAt(),
            entityJPA.getUpdatedAt()
        );
    }

    private GetInventoryDTO inventoryEntityToDto(InventoryEntityJPA entityJPA) {
        return new GetInventoryDTO(
            entityJPA.getId(),
            entityJPA.getName(),
            unitEntityToDto(entityJPA.getUnit()),
            entityJPA.getQuantity(),
            entityJPA.getMinimumQuantity(),
            entityJPA.getNotes(),
            entityJPA.getCreatedAt(),
            entityJPA.getUpdatedAt()
        );
    }

    private GetUnitDTO unitEntityToDto(UnitEntityJPA entityJPA) {
        return new GetUnitDTO(
            entityJPA.getId(),
            entityJPA.getName(),
            entityJPA.getAbbreviation());
    }
}
