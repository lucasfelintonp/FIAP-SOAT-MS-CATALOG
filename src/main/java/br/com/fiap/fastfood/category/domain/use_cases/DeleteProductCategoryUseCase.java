package br.com.fiap.fastfood.category.domain.use_cases;

import br.com.fiap.fastfood.category.application.gateways.ProductCategoryGateway;
import br.com.fiap.fastfood.category.domain.entities.ProductCategoryEntity;

public class DeleteProductCategoryUseCase {
    private final ProductCategoryGateway productCategoryGateway;

    public DeleteProductCategoryUseCase(ProductCategoryGateway productCategoryGateway) {
        this.productCategoryGateway = productCategoryGateway;
    }

    public ProductCategoryEntity run(Integer categoryId) {
        ProductCategoryEntity categoryEntity = productCategoryGateway.findById(categoryId);
        productCategoryGateway.delete(categoryEntity);
        return categoryEntity;
    }
}
