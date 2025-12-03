package br.com.fiap.fastfood.category.common.handlers;

import br.com.fiap.fastfood.category.application.dtos.CreateProductCategoryDTO;
import br.com.fiap.fastfood.category.application.dtos.ProductCategoryDTO;
import br.com.fiap.fastfood.category.application.dtos.UpdateProductCategoryDTO;
import br.com.fiap.fastfood.category.common.exceptions.ProductCategoryNotFoundException;
import br.com.fiap.fastfood.category.common.exceptions.ProductCategoryPersistenceException;
import br.com.fiap.fastfood.category.infrastructure.interfaces.ProductCategoryDatasource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductCategoryHandlerTest {

    @Mock
    private ProductCategoryDatasource datasource;

    private ProductCategoryHandler handler;

    @BeforeEach
    void setUp() {
        handler = new ProductCategoryHandler(datasource);
    }

    @Test
    void create_success_shouldReturn201WithBody() {
        var dto = new CreateProductCategoryDTO("Bebidas");
        var returned = new ProductCategoryDTO(1, "Bebidas");

        when(datasource.create(dto)).thenReturn(returned);

        ResponseEntity<ProductCategoryDTO> response = handler.create(dto);

        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().id());
        assertEquals("Bebidas", response.getBody().name());

        verify(datasource, times(1)).create(dto);
    }

    @Test
    void create_error_whenDatasourceFails_shouldThrowPersistenceException() {
        var dto = new CreateProductCategoryDTO("Bebidas");

        when(datasource.create(dto)).thenThrow(new ProductCategoryPersistenceException("Erro ao persistir categoria", new RuntimeException("sql")));

        var ex = assertThrows(ProductCategoryPersistenceException.class, () -> handler.create(dto));
        assertTrue(ex.getMessage().contains("Erro"));

        verify(datasource, times(1)).create(dto);
    }

    @Test
    void getAll_success_shouldReturn200AndList() {
        var list = List.of(
            new ProductCategoryDTO(1, "Bebidas"),
            new ProductCategoryDTO(2, "Lanches")
        );

        when(datasource.findAll()).thenReturn(list);

        ResponseEntity<List<ProductCategoryDTO>> response = handler.getAll();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Bebidas", response.getBody().getFirst().name());

        verify(datasource, times(1)).findAll();
    }

    @Test
    void getById_success_shouldReturn200AndBody() {
        var returned = new ProductCategoryDTO(2, "Lanches");

        when(datasource.findById(2)).thenReturn(returned);

        ResponseEntity<ProductCategoryDTO> response = handler.getById(2);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().id());
        assertEquals("Lanches", response.getBody().name());

        verify(datasource, times(1)).findById(2);
    }

    @Test
    void getById_notFound_shouldPropagateNotFoundException() {
        when(datasource.findById(99)).thenThrow(new IllegalArgumentException("Categoria de produto com ID 99 não encontrada."));

        assertThrows(ProductCategoryNotFoundException.class, () -> handler.getById(99));

        verify(datasource, times(1)).findById(99);
    }

    @Test
    void update_success_shouldReturn200AndUpdatedBody() {
        var dto = new UpdateProductCategoryDTO(3, "Sobremesas");
        var returned = new ProductCategoryDTO(3, "Sobremesas");

        when(datasource.update(new UpdateProductCategoryDTO(3, "Sobremesas"))).thenReturn(returned);

        ResponseEntity<ProductCategoryDTO> response = handler.update(3, dto);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().id());
        assertEquals("Sobremesas", response.getBody().name());

        verify(datasource, times(1)).update(new UpdateProductCategoryDTO(3, "Sobremesas"));
    }

    @Test
    void update_notFound_shouldPropagateNotFoundException() {
        var dto = new UpdateProductCategoryDTO(50, "Inexistente");

        when(datasource.update(new UpdateProductCategoryDTO(50, "Inexistente"))).thenThrow(new IllegalArgumentException("Categoria de produto com ID 50 não encontrada."));

        assertThrows(ProductCategoryNotFoundException.class, () -> handler.update(50, dto));

        verify(datasource, times(1)).update(new UpdateProductCategoryDTO(50, "Inexistente"));
    }

    @Test
    void delete_success_shouldReturnNoContent() {
        var returned = new ProductCategoryDTO(4, "Acompanhamentos");

        when(datasource.findById(4)).thenReturn(returned);
        doNothing().when(datasource).delete(4);

        ResponseEntity<Void> response = handler.delete(4);

        assertEquals(204, response.getStatusCode().value());

        verify(datasource, times(1)).findById(4);
        verify(datasource, times(1)).delete(4);
    }

    @Test
    void delete_notFound_shouldPropagateNotFoundException() {
        when(datasource.findById(100)).thenThrow(new IllegalArgumentException("Categoria de produto com ID 100 não encontrada."));

        assertThrows(ProductCategoryNotFoundException.class, () -> handler.delete(100));

        verify(datasource, times(1)).findById(100);
        verify(datasource, never()).delete(anyInt());
    }
}

