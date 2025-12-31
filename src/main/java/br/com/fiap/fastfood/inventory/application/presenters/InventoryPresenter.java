package br.com.fiap.fastfood.inventory.application.presenters;

import br.com.fiap.fastfood.inventory.application.dtos.GetInventoryDTO;
import br.com.fiap.fastfood.inventory.application.dtos.GetUnitDTO;
import br.com.fiap.fastfood.inventory.domain.entities.InventoryEntity;

import java.util.List;

public class InventoryPresenter {

    private InventoryPresenter() {
        throw new IllegalStateException("InventoryPresenter não pode ser instanciado, é uma classe de utilidade.");
    }

    public static List<GetInventoryDTO> searchInventoryDTO(List<InventoryEntity> items) {
        return items.stream()
            .map(InventoryPresenter::inventoryEntityToDto).toList();
    }

    public static GetInventoryDTO createInventoryDTO(InventoryEntity inventoryItem) {
        return inventoryEntityToDto(inventoryItem);
    }

    private static GetInventoryDTO inventoryEntityToDto(InventoryEntity inventoryItem) {
        return new GetInventoryDTO(
            inventoryItem.getId(),
            inventoryItem.getName(),
            new GetUnitDTO(
                inventoryItem.getUnit().getId(),
                inventoryItem.getUnit().getName(),
                inventoryItem.getUnit().getAbbreviation()
            ),
            inventoryItem.getQuantity(),
            inventoryItem.getMinimumQuantity(),
            inventoryItem.getNotes(),
            inventoryItem.getCreatedAt(),
            inventoryItem.getUpdatedAt()
        );

    }
}
