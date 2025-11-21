package br.com.fiap.fastfood.product.common.handlers;

import br.com.fiap.fastfood.product.application.controllers.ProductController;
import br.com.fiap.fastfood.product.application.dtos.CreateProductDTO;
import br.com.fiap.fastfood.product.application.dtos.ProductDTO;
import br.com.fiap.fastfood.product.application.dtos.UpdateProductDTO;
import br.com.fiap.fastfood.product.infrastructure.interfaces.ProductDatasource;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
public class ProductHandler {

    private final ProductController productController;

    public ProductHandler(
        ProductDatasource productDatasource
    ) {
        this.productController = new ProductController(
            productDatasource
        );
    }

    @PostMapping
    public ResponseEntity<ProductDTO> create(@RequestBody @Valid CreateProductDTO dto) {
        ProductDTO createdProduct = productController.create(dto);

        return ResponseEntity
            .status(201)
            .body(createdProduct);
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> findAll(
        @RequestParam(value = "categoryId", required = false) Integer categoryId
    ) {
        var products = productController.findAll(categoryId);

        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable UUID id) {
        var product = productController.findById(id);

        return ResponseEntity.ok(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(
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
    public ResponseEntity<ProductDTO> delete(@PathVariable UUID id) {
        productController.delete(id);
        return ResponseEntity
            .noContent()
            .build();
    }
}
