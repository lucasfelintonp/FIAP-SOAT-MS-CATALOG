package br.com.fiap.fastfood.inventory.domain.use_cases;

import br.com.fiap.fastfood.inventory.application.gateways.InventoryGateway;
import br.com.fiap.fastfood.inventory.domain.entities.InventoryEntity;

import java.util.List;

public class SearchInventoryUseCase {

    InventoryGateway gateway;

    public SearchInventoryUseCase(InventoryGateway gateway) {
        this.gateway = gateway;
    }

    public List<InventoryEntity> run() {
        return gateway.findAll();
    }
}
