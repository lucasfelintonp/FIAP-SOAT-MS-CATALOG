package br.com.fiap.fastfood.category.common.handlers;

import br.com.fiap.fastfood.category.application.controllers.ProductCategoryController;
import br.com.fiap.fastfood.category.application.dtos.CreateProductCategoryDTO;
import br.com.fiap.fastfood.category.application.dtos.ProductCategoryDTO;
import br.com.fiap.fastfood.category.application.dtos.UpdateProductCategoryDTO;
import br.com.fiap.fastfood.category.infrastructure.interfaces.ProductCategoryDatasource;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product-categories")
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
    public ResponseEntity<ProductCategoryDTO> create(@RequestBody @Valid CreateProductCategoryDTO dto) {
        ProductCategoryDTO createdCategory = productCategoryController.createProductCategory(dto);

        return ResponseEntity
            .status(201)
            .body(createdCategory);
    }

    @GetMapping
    public ResponseEntity<List<ProductCategoryDTO>> getAll() {
        var categories = productCategoryController.getAllProductCategories();

        return ResponseEntity
            .ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductCategoryDTO> getById(@PathVariable Integer id) {
        ProductCategoryDTO category = productCategoryController.getProductCategoryById(id);
        return ResponseEntity
            .ok(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductCategoryDTO> update(
        @PathVariable Integer id,
        @RequestBody @Valid UpdateProductCategoryDTO dto
    ) {
        ProductCategoryDTO updatedCategory = productCategoryController.updateProductCategory(
            new UpdateProductCategoryDTO(id, dto.name())
        );

        return ResponseEntity
            .ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        productCategoryController.deleteProductCategory(id);

        return ResponseEntity
            .noContent()
            .build();
    }
}
