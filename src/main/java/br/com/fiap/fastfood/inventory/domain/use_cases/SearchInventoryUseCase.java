package br.com.fiap.fastfood.inventory.domain.use_cases;

import br.com.fiap.fastfood.inventory.application.gateways.InventoryGateway;
import br.com.fiap.fastfood.inventory.domain.entities.InventoryEntity;

import java.util.List;

/**
 * Use case for searching inventory items.
 */
public class SearchInventoryUseCase {

    private final InventoryGateway gateway;

    /**
     * Constructor.
     *
     * @param gateway the inventory gateway
     */
    public SearchInventoryUseCase(final InventoryGateway gateway) {
        this.gateway = gateway;
    }

    /**
     * Executes the use case.
     *
     * @return the list of all inventory entities
     */
    public List<InventoryEntity> run() {
        return gateway.findAll();
    }
}
