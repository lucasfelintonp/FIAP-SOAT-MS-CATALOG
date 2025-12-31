package br.com.fiap.fastfood.inventory.application.controllers;

import br.com.fiap.fastfood.inventory.application.dtos.CreateInventoryEntryDTO;
import br.com.fiap.fastfood.inventory.application.dtos.ProductsQuantityDTO;
import br.com.fiap.fastfood.inventory.application.gateways.InventoryGateway;
import br.com.fiap.fastfood.inventory.application.gateways.InventoryProductGateway;
import br.com.fiap.fastfood.inventory.domain.entities.InventoryEntryEntity;
import br.com.fiap.fastfood.inventory.domain.use_cases.CreateInventoryEntryUseCase;
import br.com.fiap.fastfood.inventory.domain.use_cases.EnableElegibleProductsUseCase;
import br.com.fiap.fastfood.inventory.domain.use_cases.DiscountInventoryItemsByProductsUseCase;
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

    public void discountInventoryItemsByProducts(List<ProductsQuantityDTO> dto) {
        InventoryProductGateway gateway = new InventoryProductGateway(datasource);
        InventoryGateway inventoryGateway = new InventoryGateway(inventoryDatasource);
        ProductGateway productGateway = new ProductGateway(productDatasource);

        DiscountInventoryItemsByProductsUseCase discountInventoryItemsByProductsUseCase =
            new DiscountInventoryItemsByProductsUseCase(gateway, inventoryGateway, productGateway);

        discountInventoryItemsByProductsUseCase.run(dto);
    }

    public void createInventoryEntry(CreateInventoryEntryDTO dto) {
        InventoryGateway inventoryGateway = new InventoryGateway(inventoryDatasource);
        ProductGateway productGateway = new ProductGateway(productDatasource);

        CreateInventoryEntryUseCase createInventoryEntryUseCase = new CreateInventoryEntryUseCase(inventoryGateway);
        EnableElegibleProductsUseCase enableElegibleProductsUseCase = new EnableElegibleProductsUseCase(inventoryGateway, productGateway);

        InventoryEntryEntity inventoryEntryEntity = createInventoryEntryUseCase.run(dto);
        enableElegibleProductsUseCase.run(inventoryEntryEntity);
    }
}
