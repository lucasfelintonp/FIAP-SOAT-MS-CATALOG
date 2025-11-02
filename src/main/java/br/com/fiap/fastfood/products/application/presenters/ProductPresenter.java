package br.com.fiap.fastfood.products.application.presenters;

import br.com.fiap.fastfood.products.application.dtos.ProductDTO;
import br.com.fiap.fastfood.products.domain.entities.ProductEntity;

import java.util.List;

public class ProductPresenter {

    private ProductPresenter() {
        throw new IllegalStateException("Product presenter não pode ser instanciado, é uma classe de utilidade.");
    }

    public static ProductDTO create(ProductEntity product) {
        return new ProductDTO(
            product.id(),
            product.name(),
            product.description(),
            product.price(),
            product.getActive(),
            product.imagePath(),
            product.categoryId(),
            product.createdAt(),
            product.updatedAt()
        );
    }

    public static ProductDTO findById(ProductEntity product) {
        return new ProductDTO(
            product.id(),
            product.name(),
            product.description(),
            product.price(),
            product.getActive(),
            product.imagePath(),
            product.categoryId(),
            product.createdAt(),
            product.updatedAt()
        );
    }

    public static List<ProductDTO> findAll(List<ProductEntity> products) {
        return products.stream()
            .map(product -> new ProductDTO(
                product.id(),
                product.name(),
                product.description(),
                product.price(),
                product.getActive(),
                product.imagePath(),
                product.categoryId(),
                product.createdAt(),
                product.updatedAt()
            ))
            .toList();
    }

    public static ProductDTO update(ProductEntity product) {
        return new ProductDTO(
            product.id(),
            product.name(),
            product.description(),
            product.price(),
            product.getActive(),
            product.imagePath(),
            product.categoryId(),
            product.createdAt(),
            product.updatedAt()
        );
    }

    public static ProductDTO delete(ProductEntity product) {
        return new ProductDTO(
            product.id(),
            product.name(),
            product.description(),
            product.price(),
            product.getActive(),
            product.imagePath(),
            product.categoryId(),
            product.createdAt(),
            product.updatedAt()
        );
    }
}
