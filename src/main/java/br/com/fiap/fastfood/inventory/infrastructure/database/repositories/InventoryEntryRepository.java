package br.com.fiap.fastfood.inventory.infrastructure.database.repositories;

import br.com.fiap.fastfood.inventory.infrastructure.database.entities.InventoryEntryEntityJPA;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryEntryRepository extends JpaRepository<InventoryEntryEntityJPA, Long> {
}
