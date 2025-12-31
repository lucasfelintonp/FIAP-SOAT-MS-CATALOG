package br.com.fiap.fastfood.inventory.infrastructure.database.adapters;

import br.com.fiap.fastfood.inventory.application.dtos.GetInventoryDTO;
import br.com.fiap.fastfood.inventory.application.dtos.GetUnitDTO;
import br.com.fiap.fastfood.inventory.infrastructure.database.entities.InventoryEntityJPA;
import br.com.fiap.fastfood.inventory.infrastructure.database.entities.UnitEntityJPA;
import br.com.fiap.fastfood.inventory.infrastructure.database.repositories.InventoryRepository;
import br.com.fiap.fastfood.inventory.infrastructure.database.repositories.UnitRepository;
import br.com.fiap.fastfood.inventory.infrastructure.interfaces.InventoryDatasource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryAdapter implements InventoryDatasource {

    private final InventoryRepository inventoryRepository;
    private final UnitRepository unitRepository;

    @Autowired
    public InventoryAdapter(
        InventoryRepository inventoryRepository,
        UnitRepository unitRepository
    ){
        this.inventoryRepository = inventoryRepository;
        this.unitRepository = unitRepository;
    }

    @Override
    public List<GetInventoryDTO> findAll() {
        var inventoryItems = inventoryRepository.findAll();

        return inventoryItems.stream().map(this::inventoryEntityToDto).toList();
    }

    @Override
    public Optional<GetUnitDTO> findUnitById(Integer unitId) {
        var unit = unitRepository.findById(unitId);

        if (unit.isPresent()) {
            GetUnitDTO dto = unitEntityToDto(unit.get());
            return Optional.of(dto);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public GetInventoryDTO create(GetInventoryDTO dto) {
        var inventoryEntityJPA = dtoToInventoryEntityJPA(dto);

        var inventoryItem = inventoryRepository.save(inventoryEntityJPA);

        return inventoryEntityToDto(inventoryItem);
    }

    private GetInventoryDTO inventoryEntityToDto(InventoryEntityJPA entityJPA) {
        return new GetInventoryDTO(
            entityJPA.getId(),
            entityJPA.getName(),
            unitEntityToDto(entityJPA.getUnit()),
            entityJPA.getQuantity(),
            entityJPA.getMinimumQuantity(),
            entityJPA.getNotes(),
            entityJPA.getCreatedAt(),
            entityJPA.getUpdatedAt()
        );
    }

    private GetUnitDTO unitEntityToDto(UnitEntityJPA entityJPA) {
        return new GetUnitDTO(
            entityJPA.getId(),
            entityJPA.getName(),
            entityJPA.getAbbreviation());
    }

    private InventoryEntityJPA dtoToInventoryEntityJPA(GetInventoryDTO dto) {
        return new InventoryEntityJPA(
            dto.id(),
            dto.name(),
            new UnitEntityJPA(dto.unit().id(), dto.unit().name(), dto.unit().abbreviation()),
            dto.quantity(),
            dto.minimum_quantity(),
            dto.notes(),
            dto.created_at(),
            dto.updated_at()
        );
    }
}
