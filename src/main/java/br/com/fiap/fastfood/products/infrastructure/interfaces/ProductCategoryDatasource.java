package br.com.fiap.fastfood.products.infrastructure.interfaces;


import br.com.fiap.fastfood.products.application.dtos.ProductCategoryDTO;
import br.com.fiap.fastfood.products.application.dtos.CreateProductCategoryDTO;
import br.com.fiap.fastfood.products.application.dtos.UpdateProductCategoryDTO;

import java.util.List;

public interface ProductCategoryDatasource {
    ProductCategoryDTO createProductCategory(CreateProductCategoryDTO productCategoryDTO);

    ProductCategoryDTO getProductCategoryById(Integer id);

    List<ProductCategoryDTO> getAllProductCategories();

    ProductCategoryDTO updateProductCategory(UpdateProductCategoryDTO productCategoryDTO);

    void deleteProductCategory(Integer id);
}
