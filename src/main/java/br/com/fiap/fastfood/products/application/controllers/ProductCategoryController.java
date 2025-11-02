package br.com.fiap.fastfood.products.application.controllers;


import br.com.fiap.fastfood.products.application.dtos.CreateProductCategoryDTO;
import br.com.fiap.fastfood.products.application.dtos.ProductCategoryDTO;
import br.com.fiap.fastfood.products.application.dtos.UpdateProductCategoryDTO;
import br.com.fiap.fastfood.products.application.gateways.ProductCategoryGateway;
import br.com.fiap.fastfood.products.application.presenters.ProductCategoryPresenter;
import br.com.fiap.fastfood.products.domain.entities.ProductCategoryEntity;
import br.com.fiap.fastfood.products.domain.use_cases.*;
import br.com.fiap.fastfood.products.infrastructure.interfaces.ProductCategoryDatasource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCategoryController {
    private final ProductCategoryGateway productCategoryGateway;

    public ProductCategoryController(ProductCategoryGateway productCategoryGateway) {
        this.productCategoryGateway = productCategoryGateway;
    }

    public ProductCategoryDTO createProductCategory(CreateProductCategoryDTO productCategoryDTO) {
        CreateProductCategoryUseCase createProductCategoryUseCase = new CreateProductCategoryUseCase(productCategoryGateway);

        ProductCategoryEntity categoryEntity = createProductCategoryUseCase.run(productCategoryDTO);

        return ProductCategoryPresenter.createy(categoryEntity);
    }

    public ProductCategoryDTO getProductCategoryById(Integer categoryId) {
        GetProductCategoryUseCase getProductCategoryUseCase = new GetProductCategoryUseCase(productCategoryGateway);

        ProductCategoryEntity categoryEntity = getProductCategoryUseCase.run(categoryId);

        return ProductCategoryPresenter.create(categoryEntity);
    }

    public List<ProductCategoryDTO> getAllProductCategories() {
        GetAllProductCategoriesUseCase getAllProductCategoriesUseCase = new GetAllProductCategoriesUseCase(productCategoryGateway);

        List<ProductCategoryEntity> categories = getAllProductCategoriesUseCase.run();

        return ProductCategoryPresenter.findAll(categories);
    }

    public ProductCategoryDTO updateProductCategory(UpdateProductCategoryDTO productCategoryDTO) {
        UpdateProductCategoryUseCase updateProductCategoryUseCase = new UpdateProductCategoryUseCase(productCategoryGateway);

        ProductCategoryEntity categoryEntity = updateProductCategoryUseCase.run(productCategoryDTO);

        return ProductCategoryPresenter.create(categoryEntity);
    }

    public void deleteProductCategory(Integer categoryId) {
        DeleteProductCategoryUseCase deleteProductCategoryUseCase = new DeleteProductCategoryUseCase(productCategoryGateway);

        deleteProductCategoryUseCase.run(categoryId);
    }
}
