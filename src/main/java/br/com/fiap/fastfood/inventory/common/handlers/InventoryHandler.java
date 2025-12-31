package br.com.fiap.fastfood.inventory.common.handlers;

import br.com.fiap.fastfood.inventory.application.controllers.InventoryController;
import br.com.fiap.fastfood.inventory.application.controllers.InventoryProductController;
import br.com.fiap.fastfood.inventory.application.dtos.CreateInventoryEntryDTO;
import br.com.fiap.fastfood.inventory.application.dtos.CreateInventoryItemDTO;
import br.com.fiap.fastfood.inventory.application.dtos.GetInventoryDTO;
import br.com.fiap.fastfood.inventory.application.dtos.ProductsQuantityDTO;
import br.com.fiap.fastfood.inventory.infrastructure.interfaces.InventoryDatasource;
import br.com.fiap.fastfood.inventory.infrastructure.interfaces.InventoryProductsDatasource;
import br.com.fiap.fastfood.product.infrastructure.interfaces.ProductDatasource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Estoque", description = "Operações relacionadas ao itens de estoque")
@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryHandler {

    private final InventoryController inventoryController;
    private final InventoryProductController inventoryProductController;

    public InventoryHandler(
        InventoryDatasource inventoryDatasource,
        InventoryProductsDatasource inventoryProductsDatasource,
        ProductDatasource productDatasource
    ) {
        this.inventoryController = new InventoryController(inventoryDatasource);
        this.inventoryProductController = new InventoryProductController(inventoryProductsDatasource, inventoryDatasource, productDatasource);
    }

    @Operation(summary = "Buscar item de estoque")
    @GetMapping
    public ResponseEntity<List<GetInventoryDTO>> searchInventory() {
        return ResponseEntity.ok(inventoryController.searchInventory());
    }

    @Operation(summary = "Criar novo item de estoque")
    @PostMapping
    public ResponseEntity<GetInventoryDTO> createInventoryItem(@RequestBody CreateInventoryItemDTO dto) {
        return ResponseEntity
            .status(201)
            .body(inventoryController.createInventoryItem(dto));
    }

    @Operation(summary = "Atualizar a quantidade dos itens de estoque pelo produto", description = "Descontar itens de estoque. Espera uma lista de produtos onde 'quantity' é a quantidade de produtos a ser descontada.")
    @PatchMapping("/discount-items-by-products")
    public ResponseEntity<Void> discountInventoryItemsByProducts(@RequestBody List<ProductsQuantityDTO> dtos) {
        inventoryProductController.discountInventoryItemsByProducts(dtos);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Cadastrar lote de estoque")
    @PostMapping("/entry")
    public ResponseEntity<String> createInventoryEntry(@RequestBody CreateInventoryEntryDTO dto) {
        inventoryProductController.createInventoryEntry(dto);
        return ResponseEntity.status(201).build();
    }
}
