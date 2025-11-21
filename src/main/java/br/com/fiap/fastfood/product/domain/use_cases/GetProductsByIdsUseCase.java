package br.com.fiap.fastfood.product.domain.use_cases;

import br.com.fiap.fastfood.product.application.gateways.ProductGateway;
import br.com.fiap.fastfood.product.domain.entities.ProductEntity;

import java.util.List;
import java.util.UUID;

public class GetProductsByIdsUseCase {
    private final ProductGateway productGateway;

    public GetProductsByIdsUseCase(ProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    public List<ProductEntity> run(List<UUID> productIds) {
        return productGateway.findAllByIds(productIds);
    }
}

