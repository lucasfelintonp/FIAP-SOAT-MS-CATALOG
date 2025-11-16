package br.com.fiap.fastfood.category.infrastructure.interfaces;

import br.com.fiap.fastfood.category.application.dtos.ProductCategoryDTO;
import br.com.fiap.fastfood.category.application.dtos.CreateProductCategoryDTO;
import br.com.fiap.fastfood.category.application.dtos.UpdateProductCategoryDTO;

import java.util.List;

public interface ProductCategoryDatasource {
    ProductCategoryDTO create(CreateProductCategoryDTO productCategoryDTO);

    ProductCategoryDTO findById(Integer id);

    List<ProductCategoryDTO> findAll();

    ProductCategoryDTO update(UpdateProductCategoryDTO productCategoryDTO);

    void delete(Integer id);
}
