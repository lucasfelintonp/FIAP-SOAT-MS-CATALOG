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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
        ProductDatasource  productDatasource
    ) {
        this.inventoryController = new InventoryController(inventoryDatasource);
        this.inventoryProductController = new InventoryProductController(inventoryProductsDatasource, inventoryDatasource, productDatasource);
    }

    @Operation(summary = "Buscar item de estoque", description = "Busca uma lista dos itens de estoque")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Itens encontrados"),
        @ApiResponse(responseCode = "404", description = "Nenhum estoque encontrado")
    })
    @GetMapping
    public ResponseEntity<List<GetInventoryDTO>> searchInventory() {
        var items = inventoryController.searchInventory();

        return ResponseEntity.ok(items);
    }

    @Operation(summary = "Criar item de estoque", description = "Cadastra um novo item de estoque")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Item de estoque criado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Nenhum estoque encontrado")
    })
    @PostMapping
    public ResponseEntity<GetInventoryDTO> createInventoryItem(@RequestBody CreateInventoryItemDTO dto) {
        GetInventoryDTO createdItem = inventoryController.createInventoryItem(dto);

        return ResponseEntity
            .status(201)
            .body(createdItem);
    }

    @Operation(summary = "Atualizar a quantidade dos itens de estoque pelo produto", description = "Descontar itens de estoque. Espera uma lista de produtos onde 'quantity' é a quantidade de produtos a ser descontada.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estoque descontado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Nenhum estoque encontrado")
    })
    @PatchMapping("/discount-items-by-products")
    public ResponseEntity<String> discountInventoryItemsByProducts(@RequestBody List<ProductsQuantityDTO> dtos) {
        inventoryProductController.discountInventoryItemsByProducts(dtos);

        return ResponseEntity.ok("Estoque descontado com sucesso.");
    }

    @Operation(summary = "Cadastrar lote de estoque", description = "Acrescentar novo lote de estoque.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estoque atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Nenhum estoque encontrado")
    })
    @PostMapping("/entry")
    public ResponseEntity<String> createInventoryEntry(@RequestBody CreateInventoryEntryDTO dto) {
        inventoryProductController.createInventoryEntry(dto);

        return ResponseEntity.ok("Lote de estoque adicionado com sucesso");
    }
}
