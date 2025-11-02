package br.com.fiap.fastfood.products.infrastructure.database.adapters;

import br.com.fiap.fastfood.products.application.dtos.ProductDTO;
import br.com.fiap.fastfood.products.common.ProductsConstants;
import br.com.fiap.fastfood.products.common.exceptions.ProductCategoryNotFoundException;
import br.com.fiap.fastfood.products.common.exceptions.ProductNotFoundException;
import br.com.fiap.fastfood.products.infrastructure.database.entities.ProductCategoryEntityJPA;
import br.com.fiap.fastfood.products.infrastructure.database.entities.ProductEntityJPA;
import br.com.fiap.fastfood.products.infrastructure.database.repositories.ProductCategoryRepository;
import br.com.fiap.fastfood.products.infrastructure.database.repositories.ProductRepository;
import br.com.fiap.fastfood.products.infrastructure.interfaces.ProductDatasource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static br.com.fiap.fastfood.products.common.ProductsConstants.PRODUCT_NOT_FOUND;

@Service
public class ProductAdapter implements ProductDatasource {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository categoryRepository;

    @Autowired
    public ProductAdapter(
        ProductRepository productRepository,
        ProductCategoryRepository categoryRepository
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public ProductDTO create(ProductDTO productDTO) {
        var productEntity = new ProductEntityJPA(
            productDTO.id(),
            productDTO.name(),
            productDTO.description(),
            productDTO.price(),
            productDTO.isActive(),
            productDTO.imagePath(),
            productDTO.categoryId(),
            null,
            null,
            null
        );

        var savedProduct = productRepository.save(productEntity);

        return new ProductDTO(
            savedProduct.getId(),
            savedProduct.getName(),
            savedProduct.getDescription(),
            savedProduct.getPrice(),
            savedProduct.getIsActive(),
            savedProduct.getImagePath(),
            savedProduct.getCategoryId(),
            savedProduct.getCreatedAt(),
            savedProduct.getUpdatedAt()
        );
    }

    @Override
    public ProductDTO findById(UUID productId) {
        var product = productRepository.findById(productId).orElseThrow(
            () -> new ProductNotFoundException(
                String.format(PRODUCT_NOT_FOUND, productId)
            )
        );

        return new ProductDTO(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getIsActive(),
            product.getImagePath(),
            product.getCategoryId(),
            product.getCreatedAt(),
            product.getUpdatedAt()
        );
    }

    @Override
    public List<ProductDTO> findAll(Integer categoryId) {
        List<ProductEntityJPA> products;

        if (categoryId != null) {
            products = productRepository.findAllByCategoryIdAndDeletedAtIsNull(categoryId);
        } else {
            products = productRepository.findAllByDeletedAtIsNull();
        }

        return products.stream()
            .map(product
                    -> new ProductDTO(
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getIsActive(),
                    product.getImagePath(),
                    product.getCategoryId(),
                    product.getCreatedAt(),
                    product.getUpdatedAt()
                )
            ).toList();
    }

    @Override
    public List<ProductDTO> findAllByIds(List<UUID> productIds) {
        var products = this.productRepository.findAllById(productIds);

        return products.stream()
            .map(product
                    -> new ProductDTO(
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getIsActive(),
                    product.getImagePath(),
                    product.getCategoryId(),
                    product.getCreatedAt(),
                    product.getUpdatedAt()
                )
            ).toList();
    }

    @Override
    public void enable(UUID productId) {

        ProductEntityJPA product = productRepository.findById(productId).orElseThrow(() -> new ProductCategoryNotFoundException(
            String.format(PRODUCT_NOT_FOUND, productId)
        ));

        if (Boolean.TRUE.equals(product.getIsActive())) return;

        product.setIsActive(true);

        productRepository.save(product);

    }

    @Override
    @Transactional
    public ProductDTO update(ProductDTO productDTO) {
        ProductEntityJPA product = productRepository.findById(productDTO.id())
            .orElseThrow(() -> new ProductNotFoundException(
                String.format(PRODUCT_NOT_FOUND, productDTO.id())
            ));

        Optional.ofNullable(productDTO.name()).ifPresent(product::setName);
        Optional.ofNullable(productDTO.description()).ifPresent(product::setDescription);
        Optional.ofNullable(productDTO.price()).ifPresent(product::setPrice);
        Optional.ofNullable(productDTO.isActive()).ifPresent(product::setIsActive);
        Optional.ofNullable(productDTO.imagePath()).ifPresent(product::setImagePath);

        if (productDTO.categoryId() != null) {
            ProductCategoryEntityJPA category = categoryRepository.findById(productDTO.categoryId())
                .orElseThrow(() -> new ProductCategoryNotFoundException(
                    String.format(ProductsConstants.PRODUCT_CATEGORY_NOT_FOUND, productDTO.categoryId())
                ));
            product.setCategoryId(category.getId());
        }

        ProductEntityJPA updated = productRepository.save(product);

        return new ProductDTO(
            updated.getId(),
            updated.getName(),
            updated.getDescription(),
            updated.getPrice(),
            updated.getIsActive(),
            updated.getImagePath(),
            updated.getCategoryId(),
            updated.getCreatedAt(),
            updated.getUpdatedAt()
        );
    }

    @Override
    public ProductDTO delete(UUID productId) {
        ProductEntityJPA productEntityJPA = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(
                String.format(PRODUCT_NOT_FOUND, productId)));

        productEntityJPA.setDeletedAt(LocalDateTime.now());
        var deleted = productRepository.save(productEntityJPA);

        return new ProductDTO(
            deleted.getId(),
            deleted.getName(),
            deleted.getDescription(),
            deleted.getPrice(),
            deleted.getIsActive(),
            deleted.getImagePath(),
            deleted.getCategoryId(),
            deleted.getCreatedAt(),
            deleted.getUpdatedAt()
        );
    }

    @Override
    public void disable(UUID productId) {

        ProductEntityJPA product = productRepository.findById(productId).orElseThrow(() -> new ProductCategoryNotFoundException(
            String.format(PRODUCT_NOT_FOUND, productId)
        ));

        product.setIsActive(false);

        productRepository.save(product);
    }
}
