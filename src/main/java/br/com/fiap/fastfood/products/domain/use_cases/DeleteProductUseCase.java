package br.com.fiap.fastfood.products.domain.use_cases;

import br.com.fiap.fastfood.products.application.gateways.ProductGateway;
import br.com.fiap.fastfood.products.domain.entities.ProductEntity;

import java.util.UUID;

public class DeleteProductUseCase {
    private final ProductGateway productGateway;

    public DeleteProductUseCase(ProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    public ProductEntity run(UUID productId) {
        var product = productGateway.findById(productId);

        return productGateway.delete(product);
    }
}
