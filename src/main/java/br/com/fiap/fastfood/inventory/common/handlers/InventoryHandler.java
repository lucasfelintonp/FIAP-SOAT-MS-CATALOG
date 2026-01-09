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
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
@Tag(name = "Inventory", description = "APIs para gerenciamento de inventário")
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

    @GetMapping
    @Operation(
        summary = "Buscar itens do inventário",
        description = "Retorna uma lista com todos os itens do inventário"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de itens do inventário retornada com sucesso",
            content = @Content(schema = @Schema(implementation = GetInventoryDTO.class))
        )
    })
    public ResponseEntity<List<GetInventoryDTO>> searchInventory() {
        return ResponseEntity.ok(inventoryController.searchInventory());
    }

    @PostMapping
    @Operation(
        summary = "Criar item de inventário",
        description = "Cria um novo item no inventário"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Item de inventário criado com sucesso",
            content = @Content(schema = @Schema(implementation = GetInventoryDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos fornecidos",
            content = @Content
        )
    })
    public ResponseEntity<GetInventoryDTO> createInventoryItem(@RequestBody CreateInventoryItemDTO dto) {
        return ResponseEntity
            .status(201)
            .body(inventoryController.createInventoryItem(dto));
    }

    @PatchMapping("/discount-items-by-products")
    @Operation(
        summary = "Descontar itens do inventário por produtos",
        description = "Desconta quantidades do inventário baseado em uma lista de produtos e suas quantidades"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Itens descontados com sucesso",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos ou estoque insuficiente",
            content = @Content
        )
    })
    public ResponseEntity<Void> discountInventoryItemsByProducts(@RequestBody List<ProductsQuantityDTO> dtos) {
        inventoryProductController.discountInventoryItemsByProducts(dtos);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/entry")
    @Operation(
        summary = "Criar entrada de inventário",
        description = "Registra uma entrada de produtos no inventário"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Entrada de inventário criada com sucesso",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos fornecidos",
            content = @Content
        )
    })
    public ResponseEntity<String> createInventoryEntry(@RequestBody CreateInventoryEntryDTO dto) {
        inventoryProductController.createInventoryEntry(dto);
        return ResponseEntity.status(201).build();
    }
}
