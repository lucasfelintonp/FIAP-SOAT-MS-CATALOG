package br.com.fiap.fastfood.products.domain.use_cases;

import br.com.fiap.fastfood.products.application.dtos.UpdateProductDTO;
import br.com.fiap.fastfood.products.application.gateways.ProductGateway;
import br.com.fiap.fastfood.products.domain.entities.ProductEntity;

public class UpdateProductUseCase {
    private final ProductGateway productGateway;

    public UpdateProductUseCase(ProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    public ProductEntity run(UpdateProductDTO productDTO) {
        var productEntity = new ProductEntity(
                productDTO.id(),
                productDTO.name(),
                productDTO.description(),
                productDTO.price(),
                productDTO.isActive(),
                productDTO.imagePath(),
                productDTO.categoryId(),
                null,
                null,
                null
        );

        return productGateway.update(productEntity);
    }
}
