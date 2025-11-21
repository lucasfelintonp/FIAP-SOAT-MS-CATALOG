package br.com.fiap.fastfood.category.domain.use_cases;

import br.com.fiap.fastfood.category.application.dtos.UpdateProductCategoryDTO;
import br.com.fiap.fastfood.category.application.gateways.ProductCategoryGateway;
import br.com.fiap.fastfood.category.domain.entities.ProductCategoryEntity;

public class UpdateProductCategoryUseCase {
    private final ProductCategoryGateway productCategoryGateway;

    public UpdateProductCategoryUseCase(ProductCategoryGateway productCategoryGateway) {
        this.productCategoryGateway = productCategoryGateway;
    }

    public ProductCategoryEntity run(UpdateProductCategoryDTO updateProductCategoryDTO) {
        ProductCategoryEntity categoryEntity = new ProductCategoryEntity(
            updateProductCategoryDTO.id(),
            updateProductCategoryDTO.name()
        );

        return productCategoryGateway.update(categoryEntity);
    }
}
