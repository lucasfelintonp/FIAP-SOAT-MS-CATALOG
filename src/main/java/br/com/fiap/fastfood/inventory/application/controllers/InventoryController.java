package br.com.fiap.fastfood.inventory.application.controllers;

import br.com.fiap.fastfood.inventory.application.dtos.CreateInventoryItemDTO;
import br.com.fiap.fastfood.inventory.application.dtos.GetInventoryDTO;
import br.com.fiap.fastfood.inventory.application.gateways.InventoryGateway;
import br.com.fiap.fastfood.inventory.application.presenters.InventoryPresenter;
import br.com.fiap.fastfood.inventory.domain.use_cases.CreateInventoryItemUseCase;
import br.com.fiap.fastfood.inventory.infrastructure.interfaces.InventoryDatasource;
import br.com.fiap.fastfood.inventory.domain.use_cases.SearchInventoryUseCase;

import java.util.List;

public class InventoryController {

    private final InventoryDatasource inventoryDatasource;

    public InventoryController(InventoryDatasource inventoryDatasource) {
        this.inventoryDatasource = inventoryDatasource;
    }

    public List<GetInventoryDTO> searchInventory() {
        InventoryGateway gateway = new InventoryGateway(inventoryDatasource);

        SearchInventoryUseCase searchInventoryUseCase = new SearchInventoryUseCase(gateway);

        var inventoryItems = searchInventoryUseCase.run();

        return InventoryPresenter.searchInventoryDTO(inventoryItems);
    }

    public GetInventoryDTO createInventoryItem(CreateInventoryItemDTO dto) {
        InventoryGateway gateway = new InventoryGateway(inventoryDatasource);

        CreateInventoryItemUseCase createInventoryItemUseCase = new CreateInventoryItemUseCase(gateway);

        var item = createInventoryItemUseCase.run(dto);

        return InventoryPresenter.createInventoryDTO(item);
    }
}
