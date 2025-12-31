package br.com.fiap.fastfood.inventory.application.controllers;

import br.com.fiap.fastfood.inventory.application.dtos.CreateInventoryEntryDTO;
import br.com.fiap.fastfood.inventory.application.dtos.CreateInventoryItemDTO;
import br.com.fiap.fastfood.inventory.application.dtos.GetInventoryDTO;
import br.com.fiap.fastfood.inventory.application.dtos.ProductsQuantityDTO;
import br.com.fiap.fastfood.inventory.application.gateways.InventoryGateway;
import br.com.fiap.fastfood.inventory.application.presenters.InventoryPresenter;
import br.com.fiap.fastfood.inventory.domain.use_cases.CreateInventoryItemUseCase;
import br.com.fiap.fastfood.inventory.infrastructure.interfaces.InventoryDatasource;
import br.com.fiap.fastfood.inventory.domain.use_cases.SearchInventoryUseCase;

import java.util.List;

public class InventoryController {

    private final InventoryDatasource datasource;

    public InventoryController(InventoryDatasource datasource) {
        this.datasource = datasource;
    }

    public List<GetInventoryDTO> searchInventory() {
        InventoryGateway gateway = new InventoryGateway(datasource);
        SearchInventoryUseCase searchInventoryUseCase = new SearchInventoryUseCase(gateway);

        var inventoryItems = searchInventoryUseCase.run();
        return InventoryPresenter.searchInventoryDTO(inventoryItems);
    }

    public GetInventoryDTO createInventoryItem(CreateInventoryItemDTO dto) {
        InventoryGateway gateway = new InventoryGateway(datasource);
        CreateInventoryItemUseCase createInventoryItemUseCase = new CreateInventoryItemUseCase(gateway);

        var item = createInventoryItemUseCase.run(dto);
        return InventoryPresenter.createInventoryDTO(item);
    }
}
