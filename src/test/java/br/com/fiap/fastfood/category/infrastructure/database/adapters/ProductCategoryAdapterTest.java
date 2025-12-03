package br.com.fiap.fastfood.category.infrastructure.database.adapters;

import br.com.fiap.fastfood.category.application.dtos.CreateProductCategoryDTO;
import br.com.fiap.fastfood.category.application.dtos.UpdateProductCategoryDTO;
import br.com.fiap.fastfood.category.infrastructure.database.entities.ProductCategoryEntityJPA;
import br.com.fiap.fastfood.category.infrastructure.database.repositories.ProductCategoryRepository;
import br.com.fiap.fastfood.product.common.ProductsConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductCategoryAdapterTest {

    @Mock
    private ProductCategoryRepository repository;

    @InjectMocks
    private ProductCategoryAdapter adapter;

    private ProductCategoryEntityJPA bebidasEntity;
    private ProductCategoryEntityJPA lanchesEntity;
    private ProductCategoryEntityJPA acompanhamentosEntity;
    private ProductCategoryEntityJPA sobremesasEntity;

    @BeforeEach
    void setUp() {
        bebidasEntity = new ProductCategoryEntityJPA("Bebidas");
        bebidasEntity.setId(1);

        lanchesEntity = new ProductCategoryEntityJPA("Lanches");
        lanchesEntity.setId(2);

        acompanhamentosEntity = new ProductCategoryEntityJPA("Acompanhamentos");
        acompanhamentosEntity.setId(3);

        sobremesasEntity = new ProductCategoryEntityJPA("Sobremesas");
        sobremesasEntity.setId(4);
    }

    // ==================== CREATE TESTS ====================

    @Test
    void create_success_shouldCreateBebidas() {
        var createDto = new CreateProductCategoryDTO("Bebidas");

        when(repository.save(any(ProductCategoryEntityJPA.class))).thenReturn(bebidasEntity);

        var result = adapter.create(createDto);

        assertNotNull(result);
        assertEquals(1, result.id());
        assertEquals("Bebidas", result.name());

        ArgumentCaptor<ProductCategoryEntityJPA> captor = ArgumentCaptor.forClass(ProductCategoryEntityJPA.class);
        verify(repository, times(1)).save(captor.capture());
        assertEquals("Bebidas", captor.getValue().getName());
    }

    @Test
    void create_success_shouldCreateLanches() {
        var createDto = new CreateProductCategoryDTO("Lanches");

        when(repository.save(any(ProductCategoryEntityJPA.class))).thenReturn(lanchesEntity);

        var result = adapter.create(createDto);

        assertNotNull(result);
        assertEquals(2, result.id());
        assertEquals("Lanches", result.name());

        verify(repository, times(1)).save(any(ProductCategoryEntityJPA.class));
    }

    @Test
    void create_success_shouldCreateAcompanhamentos() {
        var createDto = new CreateProductCategoryDTO("Acompanhamentos");

        when(repository.save(any(ProductCategoryEntityJPA.class))).thenReturn(acompanhamentosEntity);

        var result = adapter.create(createDto);

        assertNotNull(result);
        assertEquals(3, result.id());
        assertEquals("Acompanhamentos", result.name());

        verify(repository, times(1)).save(any(ProductCategoryEntityJPA.class));
    }

    @Test
    void create_success_shouldCreateSobremesas() {
        var createDto = new CreateProductCategoryDTO("Sobremesas");

        when(repository.save(any(ProductCategoryEntityJPA.class))).thenReturn(sobremesasEntity);

        var result = adapter.create(createDto);

        assertNotNull(result);
        assertEquals(4, result.id());
        assertEquals("Sobremesas", result.name());

        verify(repository, times(1)).save(any(ProductCategoryEntityJPA.class));
    }

    @Test
    void create_error_whenRepositoryFails_shouldPropagateException() {
        var createDto = new CreateProductCategoryDTO("Bebidas");

        when(repository.save(any(ProductCategoryEntityJPA.class)))
                .thenThrow(new RuntimeException("Erro ao salvar no banco de dados"));

        var exception = assertThrows(RuntimeException.class, () -> adapter.create(createDto));

        assertEquals("Erro ao salvar no banco de dados", exception.getMessage());
        verify(repository, times(1)).save(any(ProductCategoryEntityJPA.class));
    }

    // ==================== FIND BY ID TESTS ====================

    @Test
    void findById_success_shouldReturnBebidas() {
        when(repository.findById(1)).thenReturn(Optional.of(bebidasEntity));

        var result = adapter.findById(1);

        assertNotNull(result);
        assertEquals(1, result.id());
        assertEquals("Bebidas", result.name());

        verify(repository, times(1)).findById(1);
    }

    @Test
    void findById_success_shouldReturnLanches() {
        when(repository.findById(2)).thenReturn(Optional.of(lanchesEntity));

        var result = adapter.findById(2);

        assertNotNull(result);
        assertEquals(2, result.id());
        assertEquals("Lanches", result.name());

        verify(repository, times(1)).findById(2);
    }

    @Test
    void findById_success_shouldReturnAcompanhamentos() {
        when(repository.findById(3)).thenReturn(Optional.of(acompanhamentosEntity));

        var result = adapter.findById(3);

        assertNotNull(result);
        assertEquals(3, result.id());
        assertEquals("Acompanhamentos", result.name());

        verify(repository, times(1)).findById(3);
    }

    @Test
    void findById_error_whenNotFound_shouldThrowIllegalArgumentException() {
        int nonExistentId = 999;
        when(repository.findById(nonExistentId)).thenReturn(Optional.empty());

        var exception = assertThrows(IllegalArgumentException.class,
                () -> adapter.findById(nonExistentId));

        assertEquals(String.format(ProductsConstants.PRODUCT_CATEGORY_NOT_FOUND, nonExistentId),
                exception.getMessage());
        assertTrue(exception.getMessage().contains("Categoria de produto com ID 999 não encontrada"));

        verify(repository, times(1)).findById(nonExistentId);
    }

    @Test
    void findById_error_whenIdIsZero_shouldThrowIllegalArgumentException() {
        when(repository.findById(0)).thenReturn(Optional.empty());

        var exception = assertThrows(IllegalArgumentException.class,
                () -> adapter.findById(0));

        assertTrue(exception.getMessage().contains("Categoria de produto com ID"));
        verify(repository, times(1)).findById(0);
    }

    @Test
    void findById_error_whenIdIsNegative_shouldThrowIllegalArgumentException() {
        when(repository.findById(-1)).thenReturn(Optional.empty());

        var exception = assertThrows(IllegalArgumentException.class,
                () -> adapter.findById(-1));

        assertTrue(exception.getMessage().contains("Categoria de produto com ID"));
        verify(repository, times(1)).findById(-1);
    }

    // ==================== FIND ALL TESTS ====================

    @Test
    void findAll_success_shouldReturnAllFastFoodCategories() {
        when(repository.findAll()).thenReturn(List.of(
                bebidasEntity,
                lanchesEntity,
                acompanhamentosEntity,
                sobremesasEntity
        ));

        var result = adapter.findAll();

        assertNotNull(result);
        assertEquals(4, result.size());

        assertTrue(result.stream().anyMatch(c -> c.name().equals("Bebidas")));
        assertTrue(result.stream().anyMatch(c -> c.name().equals("Lanches")));
        assertTrue(result.stream().anyMatch(c -> c.name().equals("Acompanhamentos")));
        assertTrue(result.stream().anyMatch(c -> c.name().equals("Sobremesas")));

        verify(repository, times(1)).findAll();
    }

    @Test
    void findAll_success_shouldReturnEmptyListWhenNoCategories() {
        when(repository.findAll()).thenReturn(List.of());

        var result = adapter.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(repository, times(1)).findAll();
    }

    @Test
    void findAll_success_shouldReturnOnlyBebidas() {
        when(repository.findAll()).thenReturn(List.of(bebidasEntity));

        var result = adapter.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Bebidas", result.getFirst().name());

        verify(repository, times(1)).findAll();
    }

    @Test
    void findAll_error_whenRepositoryFails_shouldPropagateException() {
        when(repository.findAll()).thenThrow(new RuntimeException("Erro ao buscar categorias"));

        var exception = assertThrows(RuntimeException.class, () -> adapter.findAll());

        assertEquals("Erro ao buscar categorias", exception.getMessage());
        verify(repository, times(1)).findAll();
    }

    // ==================== UPDATE TESTS ====================

    @Test
    void update_success_shouldUpdateBebidasToBebidasERefrigerantes() {
        var updateDto = new UpdateProductCategoryDTO(1, "Bebidas e Refrigerantes");
        var updatedEntity = new ProductCategoryEntityJPA("Bebidas e Refrigerantes");
        updatedEntity.setId(1);

        when(repository.findById(1)).thenReturn(Optional.of(bebidasEntity));
        when(repository.save(any(ProductCategoryEntityJPA.class))).thenReturn(updatedEntity);

        var result = adapter.update(updateDto);

        assertNotNull(result);
        assertEquals(1, result.id());
        assertEquals("Bebidas e Refrigerantes", result.name());

        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).save(any(ProductCategoryEntityJPA.class));
    }

    @Test
    void update_success_shouldUpdateLanchesToLanchesGourmet() {
        var updateDto = new UpdateProductCategoryDTO(2, "Lanches Gourmet");
        var updatedEntity = new ProductCategoryEntityJPA("Lanches Gourmet");
        updatedEntity.setId(2);

        when(repository.findById(2)).thenReturn(Optional.of(lanchesEntity));
        when(repository.save(any(ProductCategoryEntityJPA.class))).thenReturn(updatedEntity);

        var result = adapter.update(updateDto);

        assertNotNull(result);
        assertEquals(2, result.id());
        assertEquals("Lanches Gourmet", result.name());

        verify(repository, times(1)).findById(2);
        verify(repository, times(1)).save(any(ProductCategoryEntityJPA.class));
    }

    @Test
    void update_success_shouldUpdateAcompanhamentosToAcompanhamentosEPorcoes() {
        var updateDto = new UpdateProductCategoryDTO(3, "Acompanhamentos e Porções");
        var updatedEntity = new ProductCategoryEntityJPA("Acompanhamentos e Porções");
        updatedEntity.setId(3);

        when(repository.findById(3)).thenReturn(Optional.of(acompanhamentosEntity));
        when(repository.save(any(ProductCategoryEntityJPA.class))).thenReturn(updatedEntity);

        var result = adapter.update(updateDto);

        assertNotNull(result);
        assertEquals(3, result.id());
        assertEquals("Acompanhamentos e Porções", result.name());

        verify(repository, times(1)).findById(3);
        verify(repository, times(1)).save(any(ProductCategoryEntityJPA.class));
    }

    @Test
    void update_error_whenCategoryNotFound_shouldThrowIllegalArgumentException() {
        int nonExistentId = 888;
        var updateDto = new UpdateProductCategoryDTO(nonExistentId, "Categoria Inexistente");

        when(repository.findById(nonExistentId)).thenReturn(Optional.empty());

        var exception = assertThrows(IllegalArgumentException.class,
                () -> adapter.update(updateDto));

        assertEquals(String.format(ProductsConstants.PRODUCT_CATEGORY_NOT_FOUND, nonExistentId),
                exception.getMessage());
        assertTrue(exception.getMessage().contains("Categoria de produto com ID 888 não encontrada"));

        verify(repository, times(1)).findById(nonExistentId);
        verify(repository, never()).save(any(ProductCategoryEntityJPA.class));
    }

    @Test
    void update_error_whenSaveFails_shouldPropagateException() {
        var updateDto = new UpdateProductCategoryDTO(1, "Bebidas Premium");

        when(repository.findById(1)).thenReturn(Optional.of(bebidasEntity));
        when(repository.save(any(ProductCategoryEntityJPA.class)))
                .thenThrow(new RuntimeException("Erro ao atualizar no banco de dados"));

        var exception = assertThrows(RuntimeException.class, () -> adapter.update(updateDto));

        assertEquals("Erro ao atualizar no banco de dados", exception.getMessage());
        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).save(any(ProductCategoryEntityJPA.class));
    }

    // ==================== DELETE TESTS ====================

    @Test
    void delete_success_shouldDeleteBebidas() {
        when(repository.findById(1)).thenReturn(Optional.of(bebidasEntity));
        doNothing().when(repository).delete(bebidasEntity);

        assertDoesNotThrow(() -> adapter.delete(1));

        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).delete(bebidasEntity);
    }

    @Test
    void delete_success_shouldDeleteLanches() {
        when(repository.findById(2)).thenReturn(Optional.of(lanchesEntity));
        doNothing().when(repository).delete(lanchesEntity);

        assertDoesNotThrow(() -> adapter.delete(2));

        verify(repository, times(1)).findById(2);
        verify(repository, times(1)).delete(lanchesEntity);
    }

    @Test
    void delete_success_shouldDeleteAcompanhamentos() {
        when(repository.findById(3)).thenReturn(Optional.of(acompanhamentosEntity));
        doNothing().when(repository).delete(acompanhamentosEntity);

        assertDoesNotThrow(() -> adapter.delete(3));

        verify(repository, times(1)).findById(3);
        verify(repository, times(1)).delete(acompanhamentosEntity);
    }

    @Test
    void delete_success_shouldDeleteSobremesas() {
        when(repository.findById(4)).thenReturn(Optional.of(sobremesasEntity));
        doNothing().when(repository).delete(sobremesasEntity);

        assertDoesNotThrow(() -> adapter.delete(4));

        verify(repository, times(1)).findById(4);
        verify(repository, times(1)).delete(sobremesasEntity);
    }

    @Test
    void delete_error_whenCategoryNotFound_shouldThrowIllegalArgumentException() {
        int nonExistentId = 777;
        when(repository.findById(nonExistentId)).thenReturn(Optional.empty());

        var exception = assertThrows(IllegalArgumentException.class,
                () -> adapter.delete(nonExistentId));

        assertEquals(String.format(ProductsConstants.PRODUCT_CATEGORY_NOT_FOUND, nonExistentId),
                exception.getMessage());
        assertTrue(exception.getMessage().contains("Categoria de produto com ID 777 não encontrada"));

        verify(repository, times(1)).findById(nonExistentId);
        verify(repository, never()).delete(any(ProductCategoryEntityJPA.class));
    }

    @Test
    void delete_error_whenRepositoryDeleteFails_shouldPropagateException() {
        when(repository.findById(1)).thenReturn(Optional.of(bebidasEntity));
        doThrow(new RuntimeException("Erro ao deletar do banco de dados"))
                .when(repository).delete(bebidasEntity);

        var exception = assertThrows(RuntimeException.class, () -> adapter.delete(1));

        assertEquals("Erro ao deletar do banco de dados", exception.getMessage());
        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).delete(bebidasEntity);
    }

    @Test
    void delete_error_whenCategoryHasProducts_shouldPropagateConstraintViolation() {
        when(repository.findById(1)).thenReturn(Optional.of(bebidasEntity));
        doThrow(new RuntimeException("Não é possível deletar categoria com produtos associados"))
                .when(repository).delete(bebidasEntity);

        var exception = assertThrows(RuntimeException.class, () -> adapter.delete(1));

        assertTrue(exception.getMessage().contains("Não é possível deletar categoria"));
        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).delete(bebidasEntity);
    }

    // ==================== MAPPING TESTS ====================

    @Test
    void mapToProductCategoryDTO_success_shouldMapAllFields() {
        when(repository.findById(1)).thenReturn(Optional.of(bebidasEntity));

        var result = adapter.findById(1);

        assertNotNull(result);
        assertEquals(bebidasEntity.getId(), result.id());
        assertEquals(bebidasEntity.getName(), result.name());
    }

    @Test
    void findAll_success_shouldMapAllFieldsForMultipleEntities() {
        when(repository.findAll()).thenReturn(List.of(bebidasEntity, lanchesEntity));

        var result = adapter.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());

        var bebidas = result.stream()
                .filter(c -> c.id() == 1)
                .findFirst()
                .orElseThrow();
        assertEquals("Bebidas", bebidas.name());

        var lanches = result.stream()
                .filter(c -> c.id() == 2)
                .findFirst()
                .orElseThrow();
        assertEquals("Lanches", lanches.name());
    }
}

