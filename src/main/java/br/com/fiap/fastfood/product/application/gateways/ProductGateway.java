package br.com.fiap.fastfood.product.application.gateways;

import br.com.fiap.fastfood.product.application.dtos.ProductDTO;
import br.com.fiap.fastfood.product.domain.entities.ProductEntity;
import br.com.fiap.fastfood.product.infrastructure.interfaces.ProductDatasource;

import java.util.List;
import java.util.UUID;

public class ProductGateway {

    private final ProductDatasource productDatasource;

    public ProductGateway(ProductDatasource productDatasource) {
        this.productDatasource = productDatasource;
    }

    public ProductEntity findById(UUID productId) {
        var product = productDatasource.findById(productId);

        return new ProductEntity(
            product.id(),
            product.name(),
            product.description(),
            product.price(),
            product.isActive(),
            product.imagePath(),
            product.categoryId(),
            product.createdAt(),
            product.updatedAt(),
            null
        );
    }

    public List<ProductEntity> findAll(Integer categoryId) {
        var productDTOs = productDatasource.findAll(categoryId);

        return productDTOs.stream()
            .map(product
                    -> new ProductEntity(
                    product.id(),
                    product.name(),
                    product.description(),
                    product.price(),
                    product.isActive(),
                    product.imagePath(),
                    product.categoryId(),
                    product.createdAt(),
                    product.updatedAt(),
                    null
                )
            ).toList();
    }

    public List<ProductEntity> findAllByIds(List<UUID> productIds) {
        var productDTOs = productDatasource.findAllByIds(productIds);

        return productDTOs.stream()
            .map(product
                    -> new ProductEntity(
                    product.id(),
                    product.name(),
                    product.description(),
                    product.price(),
                    product.isActive(),
                    product.imagePath(),
                    product.categoryId(),
                    product.createdAt(),
                    product.updatedAt(),
                    null
                )
            ).toList();
    }

    public ProductEntity create(ProductEntity product) {
        var result = productDatasource.create(
            new ProductDTO(
                product.id(),
                product.name(),
                product.description(),
                product.price(),
                product.getActive(),
                product.imagePath(),
                product.categoryId(),
                product.createdAt(),
                product.updatedAt()
            )
        );

        return new ProductEntity(
            result.id(),
            result.name(),
            result.description(),
            result.price(),
            result.isActive(),
            result.imagePath(),
            result.categoryId(),
            result.createdAt(),
            result.updatedAt(),
            null
        );
    }

    public ProductEntity update(ProductEntity product) {
        var result = productDatasource.update(
            new ProductDTO(
                product.id(),
                product.name(),
                product.description(),
                product.price(),
                product.getActive(),
                product.imagePath(),
                product.categoryId(),
                product.createdAt(),
                product.updatedAt()
            )
        );

        return new ProductEntity(
            result.id(),
            result.name(),
            result.description(),
            result.price(),
            result.isActive(),
            result.imagePath(),
            result.categoryId(),
            result.createdAt(),
            result.updatedAt(),
            null
        );
    }

    public ProductEntity delete(ProductEntity product) {
        var result = productDatasource.delete(product.id());

        return new ProductEntity(
            result.id(),
            result.name(),
            result.description(),
            result.price(),
            result.isActive(),
            result.imagePath(),
            result.categoryId(),
            result.createdAt(),
            result.updatedAt(),
            null
        );
    }

    public void enableProduct(UUID productId) {
        productDatasource.enable(productId);
    }

    public void disableProduct(UUID productId) {
        productDatasource.disable(productId);
    }
}
