package br.com.fiap.fastfood.product.common.handlers;

import br.com.fiap.fastfood.inventory.infrastructure.interfaces.InventoryDatasource;
import br.com.fiap.fastfood.product.application.controllers.ProductController;
import br.com.fiap.fastfood.product.application.dtos.CreateProductDTO;
import br.com.fiap.fastfood.product.application.dtos.ProductDTO;
import br.com.fiap.fastfood.product.application.dtos.UpdateProductDTO;
import br.com.fiap.fastfood.product.infrastructure.interfaces.ProductDatasource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Products", description = "APIs para gerenciamento de produtos")
public class ProductHandler {

    private final ProductController productController;

    public ProductHandler(
        ProductDatasource productDatasource,
        InventoryDatasource inventoryDatasource
    ) {
        this.productController = new ProductController(
            productDatasource,
            inventoryDatasource
        );
    }

    @PostMapping
    @Operation(
        summary = "Criar novo produto",
        description = "Cria um novo produto no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Produto criado com sucesso",
            content = @Content(schema = @Schema(implementation = ProductDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos fornecidos",
            content = @Content
        )
    })
    public ResponseEntity<ProductDTO> create(@RequestBody @Valid CreateProductDTO dto) {
        ProductDTO createdProduct = productController.create(dto);

        return ResponseEntity
            .status(201)
            .body(createdProduct);
    }

    @GetMapping
    @Operation(
        summary = "Listar todos os produtos",
        description = "Retorna uma lista com todos os produtos cadastrados. Pode ser filtrado por categoria."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de produtos retornada com sucesso",
            content = @Content(schema = @Schema(implementation = ProductDTO.class))
        )
    })
    public ResponseEntity<List<ProductDTO>> findAll(
        @Parameter(description = "ID da categoria para filtrar produtos")
        @RequestParam(value = "categoryId", required = false) Integer categoryId
    ) {
        var products = productController.findAll(categoryId);

        return ResponseEntity.ok(products);
    }

    @GetMapping("/find-all-by-ids")
    @Operation(
        summary = "Buscar produtos por IDs",
        description = "Retorna uma lista de produtos filtrados por uma lista de IDs"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de produtos retornada com sucesso",
            content = @Content(schema = @Schema(implementation = ProductDTO.class))
        )
    })
    public ResponseEntity<List<ProductDTO>> findAllByIds(
        @Parameter(description = "Lista de IDs dos produtos")
        @RequestParam("ids") List<UUID> ids
    ) {
        var products = productController.findAllByIds(ids);

        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar produto por ID",
        description = "Retorna um produto específico pelo seu ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Produto encontrado com sucesso",
            content = @Content(schema = @Schema(implementation = ProductDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Produto não encontrado",
            content = @Content
        )
    })
    public ResponseEntity<ProductDTO> findById(
        @Parameter(description = "ID do produto")
        @PathVariable UUID id
    ) {
        var product = productController.findById(id);

        return ResponseEntity.ok(product);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar produto",
        description = "Atualiza os dados de um produto existente"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Produto atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = ProductDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Produto não encontrado",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos fornecidos",
            content = @Content
        )
    })
    public ResponseEntity<ProductDTO> update(
        @Parameter(description = "ID do produto")
        @PathVariable UUID id,
        @RequestBody @Valid UpdateProductDTO dto
    ) {
        ProductDTO updatedProduct = productController.update(
            new UpdateProductDTO(
                id,
                dto.name(),
                dto.description(),
                dto.price(),
                dto.isActive(),
                dto.imagePath(),
                dto.categoryId()
            )
        );

        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Deletar produto",
        description = "Remove um produto do sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Produto deletado com sucesso",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Produto não encontrado",
            content = @Content
        )
    })
    public ResponseEntity<ProductDTO> delete(
        @Parameter(description = "ID do produto")
        @PathVariable UUID id
    ) {
        productController.delete(id);
        return ResponseEntity
            .noContent()
            .build();
    }
}
