package br.com.fiap.fastfood.products.domain.use_cases;

import br.com.fiap.fastfood.products.application.gateways.ProductGateway;
import br.com.fiap.fastfood.products.domain.entities.ProductEntity;

import java.util.UUID;

public class GetProductByIdUseCase {
    private final ProductGateway productGateway;

    public GetProductByIdUseCase(ProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    public ProductEntity run(UUID productId) {
        return productGateway.findById(productId);
    }
}
