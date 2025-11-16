package br.com.fiap.fastfood.product.domain.use_cases;

import br.com.fiap.fastfood.product.application.gateways.ProductGateway;
import br.com.fiap.fastfood.product.domain.entities.ProductEntity;

import java.util.List;

public class GetAllProductsUseCase {
    private final ProductGateway productGateway;

    public GetAllProductsUseCase(ProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    public List<ProductEntity> run(Integer id) {
        return productGateway.findAll(id);
    }
}
