package br.com.fiap.fastfood.category.infrastructure.database.adapters;

import br.com.fiap.fastfood.category.application.dtos.ProductCategoryDTO;
import br.com.fiap.fastfood.category.application.dtos.CreateProductCategoryDTO;
import br.com.fiap.fastfood.category.application.dtos.UpdateProductCategoryDTO;
import br.com.fiap.fastfood.category.infrastructure.database.entities.ProductCategoryEntityJPA;
import br.com.fiap.fastfood.category.infrastructure.database.repositories.ProductCategoryRepository;
import br.com.fiap.fastfood.category.infrastructure.interfaces.ProductCategoryDatasource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import static br.com.fiap.fastfood.product.common.ProductsConstants.PRODUCT_CATEGORY_NOT_FOUND;

@Service
public class ProductCategoryAdapter implements ProductCategoryDatasource {
    private final ProductCategoryRepository productCategoryRepository;

    @Autowired
    public ProductCategoryAdapter(
            ProductCategoryRepository productCategoryRepository
    ) {
        this.productCategoryRepository = productCategoryRepository;
    }

    @Override
    public ProductCategoryDTO create(CreateProductCategoryDTO productCategoryDTO) {
        var productCategoryEntity = productCategoryRepository.save(
                new ProductCategoryEntityJPA(
                        productCategoryDTO.name()
                )
        );

        return mapToProductCategoryDTO(productCategoryEntity);
    }

    @Override
    public ProductCategoryDTO findById(Integer id) {
        var productCategoryEntity = productCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format(PRODUCT_CATEGORY_NOT_FOUND, id)
                ));

        return mapToProductCategoryDTO(productCategoryEntity);
    }

    @Override
    public List<ProductCategoryDTO> findAll() {
        var productCategories = productCategoryRepository.findAll();

        return productCategories.stream()
                .map(productCategory -> new ProductCategoryDTO(
                        productCategory.getId(),
                        productCategory.getName()
                ))
                .toList();
    }

    @Override
    public ProductCategoryDTO update(UpdateProductCategoryDTO productCategoryDTO) {
        var productCategoryEntity = productCategoryRepository.findById(productCategoryDTO.id())
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format(PRODUCT_CATEGORY_NOT_FOUND, productCategoryDTO.id())
                ));

        productCategoryEntity.setName(productCategoryDTO.name());
        productCategoryEntity = productCategoryRepository.save(productCategoryEntity);

        return mapToProductCategoryDTO(productCategoryEntity);
    }

    @Override
    public void delete(Integer id) {
        var productCategoryEntity = productCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format(PRODUCT_CATEGORY_NOT_FOUND, id)
                ));

        productCategoryRepository.delete(productCategoryEntity);
    }

    private ProductCategoryDTO mapToProductCategoryDTO(ProductCategoryEntityJPA entity) {
        return new ProductCategoryDTO(
                entity.getId(),
                entity.getName()
        );
    }
}
