package br.com.fiap.fastfood.inventory.infrastructure.interfaces;

import br.com.fiap.fastfood.inventory.application.dtos.GetInventoryDTO;
import br.com.fiap.fastfood.inventory.application.dtos.GetUnitDTO;

import java.util.List;
import java.util.Optional;

public interface InventoryDatasource {

    List<GetInventoryDTO> findAll();

    Optional<GetUnitDTO> findUnitById(Integer unitId);

    GetInventoryDTO create(GetInventoryDTO dto);
}
