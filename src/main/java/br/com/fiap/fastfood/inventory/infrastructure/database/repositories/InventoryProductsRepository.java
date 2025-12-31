package br.com.fiap.fastfood.inventory.infrastructure.database.repositories;

import br.com.fiap.fastfood.inventory.infrastructure.database.entities.InventoryProductsEntityJPA;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InventoryProductsRepository extends JpaRepository<InventoryProductsEntityJPA, UUID> {
    List<InventoryProductsEntityJPA> findByProductId(UUID productId);
    List<InventoryProductsEntityJPA> findByInventoryId(UUID inventoryId);
}
