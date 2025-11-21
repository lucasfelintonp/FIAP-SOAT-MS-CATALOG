package br.com.fiap.fastfood.category.application.gateways;

import br.com.fiap.fastfood.category.application.dtos.CreateProductCategoryDTO;
import br.com.fiap.fastfood.category.application.dtos.ProductCategoryDTO;
import br.com.fiap.fastfood.category.application.dtos.UpdateProductCategoryDTO;
import br.com.fiap.fastfood.category.domain.entities.ProductCategoryEntity;
import br.com.fiap.fastfood.category.infrastructure.interfaces.ProductCategoryDatasource;

import java.util.List;

public class ProductCategoryGateway {
    private final ProductCategoryDatasource datasource;

    public ProductCategoryGateway(ProductCategoryDatasource datasource) {
        this.datasource = datasource;
    }

    public ProductCategoryEntity create(ProductCategoryEntity category) {
        var result = datasource.create(new CreateProductCategoryDTO(
            category.name()
        ));

        return mapToProductCategoryEntity(result);
    }

    public ProductCategoryEntity findById(Integer categoryId) {
        var result = datasource.findById(categoryId);

        return mapToProductCategoryEntity(result);
    }

    public List<ProductCategoryEntity> findAll() {
        var result = datasource.findAll();

        return result.stream()
            .map(category -> new ProductCategoryEntity(
                category.id(),
                category.name()
            ))
            .toList();
    }

    public ProductCategoryEntity update(ProductCategoryEntity category) {
        var result = datasource.update(
            new UpdateProductCategoryDTO(
                category.id(),
                category.name()
            ));

        return mapToProductCategoryEntity(result);
    }

    public void delete(ProductCategoryEntity category) {
        datasource.delete(category.id());
    }

    private ProductCategoryEntity mapToProductCategoryEntity(ProductCategoryDTO dto) {
        return new ProductCategoryEntity(
            dto.id(),
            dto.name()
        );
    }

}
