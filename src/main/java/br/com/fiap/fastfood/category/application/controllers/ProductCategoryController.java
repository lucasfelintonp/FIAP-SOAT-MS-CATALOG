package br.com.fiap.fastfood.category.application.controllers;

import br.com.fiap.fastfood.category.application.dtos.CreateProductCategoryDTO;
import br.com.fiap.fastfood.category.application.dtos.ProductCategoryDTO;
import br.com.fiap.fastfood.category.application.dtos.UpdateProductCategoryDTO;
import br.com.fiap.fastfood.category.application.gateways.ProductCategoryGateway;
import br.com.fiap.fastfood.category.application.presenters.ProductCategoryPresenter;
import br.com.fiap.fastfood.category.domain.entities.ProductCategoryEntity;
import br.com.fiap.fastfood.category.domain.use_cases.*;
import br.com.fiap.fastfood.category.infrastructure.interfaces.ProductCategoryDatasource;

import java.util.List;

public class ProductCategoryController {
    private final ProductCategoryDatasource productCategoryDatasource;

    public ProductCategoryController(ProductCategoryDatasource productCategoryDatasource) {
        this.productCategoryDatasource = productCategoryDatasource;
    }

    public ProductCategoryDTO createProductCategory(CreateProductCategoryDTO productCategoryDTO) {
        ProductCategoryGateway productCategoryGateway = new ProductCategoryGateway(productCategoryDatasource);

        CreateProductCategoryUseCase createProductCategoryUseCase = new CreateProductCategoryUseCase(productCategoryGateway);

        ProductCategoryEntity categoryEntity = createProductCategoryUseCase.run(productCategoryDTO);

        return ProductCategoryPresenter.create(categoryEntity);
    }

    public ProductCategoryDTO getProductCategoryById(Integer categoryId) {
        ProductCategoryGateway productCategoryGateway = new ProductCategoryGateway(productCategoryDatasource);

        GetProductCategoryUseCase getProductCategoryUseCase = new GetProductCategoryUseCase(productCategoryGateway);

        ProductCategoryEntity categoryEntity = getProductCategoryUseCase.run(categoryId);

        return ProductCategoryPresenter.create(categoryEntity);
    }

    public List<ProductCategoryDTO> getAllProductCategories() {
        ProductCategoryGateway productCategoryGateway = new ProductCategoryGateway(productCategoryDatasource);

        GetAllProductCategoriesUseCase getAllProductCategoriesUseCase = new GetAllProductCategoriesUseCase(productCategoryGateway);

        List<ProductCategoryEntity> categories = getAllProductCategoriesUseCase.run();

        return ProductCategoryPresenter.findAll(categories);
    }

    public ProductCategoryDTO updateProductCategory(UpdateProductCategoryDTO productCategoryDTO) {
        ProductCategoryGateway productCategoryGateway = new ProductCategoryGateway(productCategoryDatasource);

        UpdateProductCategoryUseCase updateProductCategoryUseCase = new UpdateProductCategoryUseCase(productCategoryGateway);

        ProductCategoryEntity categoryEntity = updateProductCategoryUseCase.run(productCategoryDTO);

        return ProductCategoryPresenter.create(categoryEntity);
    }

    public void deleteProductCategory(Integer categoryId) {
        ProductCategoryGateway productCategoryGateway = new ProductCategoryGateway(productCategoryDatasource);

        DeleteProductCategoryUseCase deleteProductCategoryUseCase = new DeleteProductCategoryUseCase(productCategoryGateway);

        deleteProductCategoryUseCase.run(categoryId);
    }
}
