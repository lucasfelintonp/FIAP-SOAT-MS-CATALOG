package br.com.fiap.fastfood.inventory.infrastructure.database.repositories;

import br.com.fiap.fastfood.inventory.infrastructure.database.entities.InventoryEntityJPA;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InventoryRepository extends JpaRepository<InventoryEntityJPA, UUID> {
}
