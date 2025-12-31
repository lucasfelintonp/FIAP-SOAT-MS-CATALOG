package br.com.fiap.fastfood.inventory.application.controllers;

import br.com.fiap.fastfood.inventory.application.dtos.ProductsQuantityDTO;
import br.com.fiap.fastfood.inventory.application.gateways.InventoryGateway;
import br.com.fiap.fastfood.inventory.application.gateways.InventoryProductGateway;
import br.com.fiap.fastfood.inventory.domain.use_cases.GetInventoryByProducts;
import br.com.fiap.fastfood.inventory.infrastructure.interfaces.InventoryDatasource;
import br.com.fiap.fastfood.inventory.infrastructure.interfaces.InventoryProductsDatasource;
import br.com.fiap.fastfood.product.application.gateways.ProductGateway;
import br.com.fiap.fastfood.product.infrastructure.interfaces.ProductDatasource;

import java.util.List;

public class InventoryProductController {

    private final InventoryProductsDatasource datasource;
    private final InventoryDatasource inventoryDatasource;
    private final ProductDatasource productDatasource;

    public InventoryProductController(
        InventoryProductsDatasource datasource,
        InventoryDatasource inventoryDatasource,
        ProductDatasource productDatasource
    ) {
        this.datasource = datasource;
        this.inventoryDatasource = inventoryDatasource;
        this.productDatasource = productDatasource;
    }

    public void discountInventoryItemsByProducts(List<ProductsQuantityDTO> dto){
        InventoryProductGateway gateway = new InventoryProductGateway(datasource);
        InventoryGateway inventoryGateway = new InventoryGateway(inventoryDatasource);
        ProductGateway productGateway = new ProductGateway(productDatasource);

        GetInventoryByProducts getInventoryByProducts = new GetInventoryByProducts(gateway, inventoryGateway, productGateway);

        getInventoryByProducts.run(dto);
    }
}
