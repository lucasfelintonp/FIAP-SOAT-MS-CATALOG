package br.com.fiap.fastfood.category.infrastructure.database.repositories;

import br.com.fiap.fastfood.category.infrastructure.database.entities.ProductCategoryEntityJPA;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategoryEntityJPA, Integer> {

}
