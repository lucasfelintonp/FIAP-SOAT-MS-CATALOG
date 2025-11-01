package br.com.fiap.fastfood.products.infrastructure.interfaces;

import br.com.fiap.fastfood.products.application.dtos.ProductDTO;

import java.util.List;
import java.util.UUID;

public interface ProductDatasource {

    ProductDTO createProduct(ProductDTO productDTO);

    ProductDTO getProductById(UUID productId);

    List<ProductDTO> getAllProducts(Integer categoryId);

    List<ProductDTO> getAllProductsByIds(List<UUID> productIds);

    ProductDTO updateProduct(ProductDTO productDTO);

    ProductDTO deleteProduct(UUID productId);

    void disableProduct(UUID productId);

    void enableProduct(UUID productId);
}
