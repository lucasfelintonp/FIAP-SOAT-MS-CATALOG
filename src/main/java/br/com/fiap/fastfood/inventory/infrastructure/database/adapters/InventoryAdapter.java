package br.com.fiap.fastfood.inventory.infrastructure.database.adapters;

import br.com.fiap.fastfood.inventory.application.dtos.*;
import br.com.fiap.fastfood.inventory.common.exceptions.EntityNotFoundException;
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
            InventoryEntryRepository inventoryEntryRepository, InventoryProductsRepository inventoryProductsRepository) {
        this.inventoryRepository = inventoryRepository;
        this.unitRepository = unitRepository;
        this.inventoryEntryRepository = inventoryEntryRepository;
        this.inventoryProductsRepository = inventoryProductsRepository;
    }

    @Override
    public List<GetInventoryDTO> findAll() {
        return inventoryRepository.findAll().stream()
                .map(this::inventoryEntityToDto)
                .toList();
    }

    @Override
    public Optional<GetUnitDTO> findUnitById(Integer unitId) {
        return unitRepository.findById(unitId)
                .map(this::unitEntityToDto);
    }

    @Override
    public GetInventoryDTO create(GetInventoryDTO dto) {
        var unit = unitRepository.findById(dto.unit().id())
                .orElseThrow(() -> new EntityNotFoundException("Unidade não encontrada ID: " + dto.unit().id()));

        var inventoryEntityJPA = new InventoryEntityJPA(
                null, // ID nulo para nova inserção
                dto.name(),
                unit,
                dto.quantity(),
                dto.minimum_quantity(),
                dto.notes(),
                dto.created_at(),
                dto.updated_at()
        );

        return inventoryEntityToDto(inventoryRepository.save(inventoryEntityJPA));
    }

    @Override
    public GetInventoryDTO getById(UUID id) {
        return inventoryRepository.findById(id)
                .map(this::inventoryEntityToDto)
                .orElseThrow(() -> new EntityNotFoundException("Item de estoque não existe ID: " + id));
    }

    @Override
    public void update(GetInventoryDTO dto) {
        var existingItem = inventoryRepository.findById(dto.id())
                .orElseThrow(() -> new EntityNotFoundException("Item não encontrado para atualização ID: " + dto.id()));

        existingItem.setName(dto.name());
        existingItem.setQuantity(dto.quantity());
        existingItem.setMinimumQuantity(dto.minimum_quantity());
        existingItem.setNotes(dto.notes());
        existingItem.setUpdatedAt(dto.updated_at());

        if (!existingItem.getUnit().getId().equals(dto.unit().id())) {
            var unit = unitRepository.findById(dto.unit().id())
                    .orElseThrow(() -> new EntityNotFoundException("Nova unidade não encontrada"));
            existingItem.setUnit(unit);
        }

        inventoryRepository.save(existingItem);
    }

    @Override
    public GetInventoryEntryDTO createInventoryEntry(CreateInventoryEntryDTO dto) {
        var inventory = inventoryRepository.findById(dto.inventoryId())
                .orElseThrow(() -> new EntityNotFoundException("Item de estoque não encontrado ID: " + dto.inventoryId()));

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
        return inventoryProductsRepository.findByInventoryId(inventoryId).stream()
                .map(this::inventoryProductEntityToDto)
                .toList();
    }

    @Override
    public List<GetInventoryProductDTO> getInventoryProductByProductId(UUID productId) {
        return inventoryProductsRepository.findByProductId(productId).stream()
                .map(this::inventoryProductEntityToDto)
                .toList();
    }

    @Override
    public void createInventoryProduct(PersistInventoryProductDTO dto) {
        InventoryEntityJPA inventory = inventoryRepository.findById(dto.inventoryId())
            .orElseThrow(() -> new RuntimeException("Item de estoque de ID " + dto.inventoryId() + " não foi encontrado"));

        inventoryProductsRepository.save(new InventoryProductsEntityJPA(
            dto.id(),
            dto.productId(),
            inventory,
            dto.quantity(),
            dto.createdAt(),
            dto.updatedAt()
        ));
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
        return new GetUnitDTO(entityJPA.getId(), entityJPA.getName(), entityJPA.getAbbreviation());
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
