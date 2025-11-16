package br.com.fiap.fastfood.product.domain.use_cases;

import br.com.fiap.fastfood.product.application.dtos.CreateProductDTO;
import br.com.fiap.fastfood.product.application.gateways.ProductGateway;
import br.com.fiap.fastfood.product.common.exceptions.ProductInvalidRequestException;
import br.com.fiap.fastfood.product.domain.entities.ProductEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static br.com.fiap.fastfood.product.common.ProductsConstants.*;

public class CreateProductUseCase {
    private final ProductGateway productGateway;

    public CreateProductUseCase(ProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    public ProductEntity run(CreateProductDTO dto) {
        validateRequest(dto);

        ProductEntity productEntity = new ProductEntity(
            null,
            dto.name(),
            dto.description(),
            dto.price(),
            dto.isActive(),
            dto.imagePath(),
            dto.categoryId(),
            null,
            null,
            null
        );

        return productGateway.create(productEntity);
    }

    private void validateRequest(CreateProductDTO dto) {
        Optional.ofNullable(dto.name())
            .filter(name -> !name.isBlank())
            .orElseThrow(() -> new ProductInvalidRequestException(PRODUCT_INVALID_NAME));

        Optional.ofNullable(dto.description())
            .filter(desc -> !desc.isBlank())
            .orElseThrow(() -> new ProductInvalidRequestException(PRODUCT_INVALID_DESCRIPTION));

        Optional.ofNullable(dto.price())
            .filter(price -> price.compareTo(BigDecimal.ZERO) > 0)
            .orElseThrow(() -> new ProductInvalidRequestException(PRODUCT_INVALID_PRICE));

        Optional.ofNullable(dto.isActive())
            .orElseThrow(() -> new ProductInvalidRequestException(PRODUCT_INVALID_IS_ACTIVE));

        Optional.ofNullable(dto.categoryId())
            .orElseThrow(() -> new ProductInvalidRequestException(PRODUCT_INVALID_CATEGORY));
    }
}
