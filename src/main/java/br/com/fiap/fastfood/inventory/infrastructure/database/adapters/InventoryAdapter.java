package br.com.fiap.fastfood.inventory.infrastructure.database.adapters;

import br.com.fiap.fastfood.inventory.application.dtos.*;
import br.com.fiap.fastfood.inventory.infrastructure.database.entities.InventoryEntityJPA;
import br.com.fiap.fastfood.inventory.infrastructure.database.entities.InventoryEntryEntityJPA;
import br.com.fiap.fastfood.inventory.infrastructure.database.entities.InventoryProductsEntityJPA;
import br.com.fiap.fastfood.inventory.infrastructure.database.entities.UnitEntityJPA;
import br.com.fiap.fastfood.inventory.infrastructure.database.repositories.InventoryEntryRepository;
import br.com.fiap.fastfood.inventory.infrastructure.database.repositories.InventoryProductsRepository;
import br.com.fiap.fastfood.inventory.infrastructure.database.repositories.InventoryRepository;
import br.com.fiap.fastfood.inventory.infrastructure.database.repositories.UnitRepository;
import br.com.fiap.fastfood.inventory.infrastructure.interfaces.InventoryDatasource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InventoryAdapter implements InventoryDatasource {

    private final InventoryRepository inventoryRepository;
    private final UnitRepository unitRepository;
    private final InventoryEntryRepository inventoryEntryRepository;
    private final InventoryProductsRepository inventoryProductsRepository;

    @Autowired
    public InventoryAdapter(
            InventoryRepository inventoryRepository,
            UnitRepository unitRepository,
            InventoryEntryRepository inventoryEntryRepository, InventoryProductsRepository inventoryProductsRepository){
        this.inventoryRepository = inventoryRepository;
        this.unitRepository = unitRepository;
        this.inventoryEntryRepository = inventoryEntryRepository;
        this.inventoryProductsRepository = inventoryProductsRepository;
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

    @Override
    public GetInventoryDTO getById(UUID id) {
        var itemJPA = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item de estoque de ID " + id + " não existe"));

        return inventoryEntityToDto(itemJPA);
    }

    @Override
    public void update(GetInventoryDTO dto) {
        var inventoryEntityJPA = dtoToInventoryEntityJPA(dto);

        inventoryRepository.save(inventoryEntityJPA);
    }

    @Override
    public GetInventoryEntryDTO createInventoryEntry(CreateInventoryEntryDTO dto) {
        var inventory = inventoryRepository.findById(dto.inventoryId()).orElseThrow(
                () -> new RuntimeException("Item de estoque com ID " + dto.inventoryId() + " não encontrado.")
        );

        var result = inventoryEntryRepository.save(new InventoryEntryEntityJPA(
                null,
                inventory,
                dto.quantity(),
                dto.expirationDate(),
                dto.entryDate()

        ));

        return new GetInventoryEntryDTO(
                result.getId(),
                inventoryEntityToDto(result.getInventory()),
                result.getQuantity(),
                result.getExpirationDate(),
                result.getEntryDate()
        );
    }

    @Override
    public List<GetInventoryProductDTO> getInventoryProductByInventoryId(UUID inventoryId) {
        var result = inventoryProductsRepository.findByInventoryId(inventoryId);

        return result.stream().map(this::inventoryProductEntityToDto).toList();
    }

    @Override
    public List<GetInventoryProductDTO> getInventoryProductByProductId(UUID productId) {
        var result = inventoryProductsRepository.findByProductId(productId);

        return result.stream().map(this::inventoryProductEntityToDto).toList();
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

    private GetInventoryProductDTO inventoryProductEntityToDto(InventoryProductsEntityJPA entityJPA) {
        return new GetInventoryProductDTO(
                entityJPA.getId(),
                entityJPA.getProductId(),
                inventoryEntityToDto(entityJPA.getInventory()),
                entityJPA.getQuantity(),
                entityJPA.getCreatedAt(),
                entityJPA.getUpdatedAt()
        );
    }
}
