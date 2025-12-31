package br.com.fiap.fastfood.inventory.infrastructure.database.repositories;

import br.com.fiap.fastfood.inventory.infrastructure.database.entities.UnitEntityJPA;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitRepository extends JpaRepository<UnitEntityJPA, Integer> {
}
