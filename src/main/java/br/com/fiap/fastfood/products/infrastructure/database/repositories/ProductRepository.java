package br.com.fiap.fastfood.products.infrastructure.database.repositories;

import br.com.fiap.fastfood.products.infrastructure.database.entities.ProductEntityJPA;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<ProductEntityJPA, UUID> {
    List<ProductEntityJPA> findAllByDeletedAtIsNull();

    Optional<ProductEntityJPA> findByIdAndDeletedAtIsNull(UUID uuid);

    List<ProductEntityJPA> findAllByCategoryIdAndDeletedAtIsNull(Integer categoryId);
}
