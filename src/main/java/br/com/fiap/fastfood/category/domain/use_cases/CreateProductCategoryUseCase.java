package br.com.fiap.fastfood.category.domain.use_cases;

import br.com.fiap.fastfood.category.application.dtos.CreateProductCategoryDTO;
import br.com.fiap.fastfood.category.application.gateways.ProductCategoryGateway;
import br.com.fiap.fastfood.category.domain.entities.ProductCategoryEntity;

public class CreateProductCategoryUseCase {
    private final ProductCategoryGateway productCategoryGateway;

    public CreateProductCategoryUseCase(ProductCategoryGateway productCategoryGateway) {
        this.productCategoryGateway = productCategoryGateway;
    }

    public ProductCategoryEntity run(CreateProductCategoryDTO createProductCategoryDTO) {
        var categoryEntity = new ProductCategoryEntity(
                null,
                createProductCategoryDTO.name()
        );

        return productCategoryGateway.create(categoryEntity);
    }
}
