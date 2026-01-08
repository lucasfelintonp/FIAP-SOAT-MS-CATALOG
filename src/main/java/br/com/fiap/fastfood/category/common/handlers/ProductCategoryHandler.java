package br.com.fiap.fastfood.category.common.handlers;

import br.com.fiap.fastfood.category.application.controllers.ProductCategoryController;
import br.com.fiap.fastfood.category.application.dtos.CreateProductCategoryDTO;
import br.com.fiap.fastfood.category.application.dtos.ProductCategoryDTO;
import br.com.fiap.fastfood.category.application.dtos.UpdateProductCategoryDTO;
import br.com.fiap.fastfood.category.common.exceptions.ProductCategoryNotFoundException;
import br.com.fiap.fastfood.category.infrastructure.interfaces.ProductCategoryDatasource;
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

@RestController
@RequestMapping("/api/v1/product-categories")
@Tag(name = "Product Categories", description = "APIs para gerenciamento de categorias de produtos")
public class ProductCategoryHandler {

    private final ProductCategoryController productCategoryController;

    public ProductCategoryHandler(
        ProductCategoryDatasource productCategoryDatasource
    ) {
        this.productCategoryController = new ProductCategoryController(
            productCategoryDatasource
        );
    }

    @PostMapping
    @Operation(
        summary = "Criar nova categoria",
        description = "Cria uma nova categoria de produto no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Categoria criada com sucesso",
            content = @Content(schema = @Schema(implementation = ProductCategoryDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos fornecidos",
            content = @Content
        )
    })
    public ResponseEntity<ProductCategoryDTO> create(@RequestBody @Valid CreateProductCategoryDTO dto) {
        ProductCategoryDTO createdCategory = productCategoryController.createProductCategory(dto);

        return ResponseEntity
            .status(201)
            .body(createdCategory);
    }

    @GetMapping
    @Operation(
        summary = "Listar todas as categorias",
        description = "Retorna uma lista com todas as categorias de produtos cadastradas"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de categorias retornada com sucesso",
            content = @Content(schema = @Schema(implementation = ProductCategoryDTO.class))
        )
    })
    public ResponseEntity<List<ProductCategoryDTO>> getAll() {
        var categories = productCategoryController.getAllProductCategories();

        return ResponseEntity
            .ok(categories);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar categoria por ID",
        description = "Retorna uma categoria específica pelo seu ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Categoria encontrada com sucesso",
            content = @Content(schema = @Schema(implementation = ProductCategoryDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Categoria não encontrada",
            content = @Content
        )
    })
    public ResponseEntity<ProductCategoryDTO> getById(
        @Parameter(description = "ID da categoria") @PathVariable Integer id
    ) {
        try {
            ProductCategoryDTO category = productCategoryController.getProductCategoryById(id);
            return ResponseEntity.ok(category);
        } catch (IllegalArgumentException ex) {
            throw new ProductCategoryNotFoundException(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar categoria",
        description = "Atualiza os dados de uma categoria existente"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Categoria atualizada com sucesso",
            content = @Content(schema = @Schema(implementation = ProductCategoryDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Categoria não encontrada",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos fornecidos",
            content = @Content
        )
    })
    public ResponseEntity<ProductCategoryDTO> update(
        @Parameter(description = "ID da categoria") @PathVariable Integer id,
        @RequestBody @Valid UpdateProductCategoryDTO dto
    ) {
        try {
            ProductCategoryDTO updatedCategory = productCategoryController.updateProductCategory(
                new UpdateProductCategoryDTO(id, dto.name())
            );

            return ResponseEntity
                .ok(updatedCategory);
        } catch (IllegalArgumentException ex) {
            throw new ProductCategoryNotFoundException(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Deletar categoria",
        description = "Remove uma categoria do sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Categoria deletada com sucesso",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Categoria não encontrada",
            content = @Content
        )
    })
    public ResponseEntity<Void> delete(
        @Parameter(description = "ID da categoria") @PathVariable Integer id
    ) {
        try {
            productCategoryController.deleteProductCategory(id);

            return ResponseEntity
                .noContent()
                .build();
        } catch (IllegalArgumentException ex) {
            throw new ProductCategoryNotFoundException(ex.getMessage());
        }
    }
}
