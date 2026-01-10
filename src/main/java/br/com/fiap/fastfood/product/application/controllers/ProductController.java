package br.com.fiap.fastfood.product.application.controllers;

import br.com.fiap.fastfood.inventory.application.gateways.InventoryGateway;
import br.com.fiap.fastfood.inventory.domain.use_cases.CreateInventoryProductsUseCase;
import br.com.fiap.fastfood.inventory.infrastructure.interfaces.InventoryDatasource;
import br.com.fiap.fastfood.product.application.dtos.CreateProductDTO;
import br.com.fiap.fastfood.product.application.dtos.ProductDTO;
import br.com.fiap.fastfood.product.application.dtos.UpdateProductDTO;
import br.com.fiap.fastfood.product.application.gateways.ProductGateway;
import br.com.fiap.fastfood.product.application.presenters.ProductPresenter;
import br.com.fiap.fastfood.product.domain.entities.ProductEntity;
import br.com.fiap.fastfood.product.domain.use_cases.*;
import br.com.fiap.fastfood.product.infrastructure.interfaces.ProductDatasource;

import java.util.List;
import java.util.UUID;

public class ProductController {
    private final ProductDatasource productDatasource;
    private final InventoryDatasource inventoryDatasource;

    public ProductController(
        ProductDatasource productDatasource,
        InventoryDatasource inventoryDatasource
    ) {
        this.productDatasource = productDatasource;
        this.inventoryDatasource = inventoryDatasource;
    }

    public ProductDTO create(CreateProductDTO productDTO) {
        ProductGateway productGateway = new ProductGateway(productDatasource);
        InventoryGateway inventoryGateway = new InventoryGateway(inventoryDatasource);

        CreateProductUseCase createProductUseCase = new CreateProductUseCase(productGateway);
        CreateInventoryProductsUseCase createInventoryProductsUseCase = new CreateInventoryProductsUseCase(inventoryGateway);

        ProductEntity productEntity = createProductUseCase.run(productDTO);
        createInventoryProductsUseCase.run(productEntity, productDTO);

        return ProductPresenter.create(productEntity);
    }

    public List<ProductDTO> findAll(Integer categoryId) {
        ProductGateway productGateway = new ProductGateway(productDatasource);

        GetAllProductsUseCase getAllProductsUseCase = new GetAllProductsUseCase(productGateway);

        List<ProductEntity> products = getAllProductsUseCase.run(categoryId);

        return ProductPresenter.findAll(products);
    }

    public List<ProductDTO> findAllByIds(final List<UUID> productIds) {
        ProductGateway productGateway = new ProductGateway(productDatasource);

        GetProductsByIdsUseCase getProductsByIdsUseCase = new GetProductsByIdsUseCase(productGateway);

        // Defensive copy to prevent internal representation exposure
        List<ProductEntity> products = getProductsByIdsUseCase.run(
            productIds != null ? List.copyOf(productIds) : List.of()
        );

        return ProductPresenter.findAll(products);
    }

    public ProductDTO findById(UUID productId) {
        ProductGateway productGateway = new ProductGateway(productDatasource);

        GetProductByIdUseCase getProductByIdUseCase = new GetProductByIdUseCase(productGateway);

        ProductEntity product = getProductByIdUseCase.run(productId);

        return ProductPresenter.findById(product);
    }

    public ProductDTO update(UpdateProductDTO productDTO) {
        ProductGateway productGateway = new ProductGateway(productDatasource);

        UpdateProductUseCase createProductUseCase = new UpdateProductUseCase(productGateway);

        ProductEntity productEntity = createProductUseCase.run(productDTO);

        return ProductPresenter.update(productEntity);
    }

    public ProductDTO delete(UUID productId) {

        ProductGateway productGateway = new ProductGateway(productDatasource);

        DeleteProductUseCase deleteProductUseCase = new DeleteProductUseCase(productGateway);

        ProductEntity productEntity = deleteProductUseCase.run(productId);

        return ProductPresenter.delete(productEntity);
    }

}
