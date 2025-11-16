package br.com.fiap.fastfood.product.infrastructure.interfaces;

import br.com.fiap.fastfood.product.application.dtos.ProductDTO;

import java.util.List;
import java.util.UUID;

public interface ProductDatasource {

    ProductDTO create(ProductDTO productDTO);

    ProductDTO findById(UUID productId);

    List<ProductDTO> findAll(Integer categoryId);

    List<ProductDTO> findAllByIds(List<UUID> productIds);

    ProductDTO update(ProductDTO productDTO);

    ProductDTO delete(UUID productId);

    void disable(UUID productId);

    void enable(UUID productId);
}
