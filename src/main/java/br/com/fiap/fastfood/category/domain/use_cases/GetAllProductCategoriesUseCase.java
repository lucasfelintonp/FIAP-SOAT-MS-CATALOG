package br.com.fiap.fastfood.category.domain.use_cases;

import br.com.fiap.fastfood.category.application.gateways.ProductCategoryGateway;
import br.com.fiap.fastfood.category.domain.entities.ProductCategoryEntity;

import java.util.List;

public class GetAllProductCategoriesUseCase {
    private final ProductCategoryGateway productCategoryGateway;

    public GetAllProductCategoriesUseCase(ProductCategoryGateway productCategoryGateway) {
        this.productCategoryGateway = productCategoryGateway;
    }

    public List<ProductCategoryEntity> run() {
        return productCategoryGateway.findAll();
    }
}
