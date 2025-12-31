package br.com.fiap.fastfood.inventory.infrastructure.interfaces;

import br.com.fiap.fastfood.inventory.application.dtos.GetInventoryProductDTO;

import java.util.List;
import java.util.UUID;

public interface InventoryProductsDatasource {

    List<GetInventoryProductDTO> getInventoryProductByProductId(UUID productId);

    List<GetInventoryProductDTO> getInventoryProductByInventoryId(UUID inventoryId);
}
