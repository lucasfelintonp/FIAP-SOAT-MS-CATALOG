package br.com.fiap.fastfood.product.application.controllers;

import br.com.fiap.fastfood.inventory.infrastructure.interfaces.InventoryDatasource;
import br.com.fiap.fastfood.product.application.dtos.CreateProductDTO;
import br.com.fiap.fastfood.product.application.dtos.ProductDTO;
import br.com.fiap.fastfood.product.application.dtos.UpdateProductDTO;
import br.com.fiap.fastfood.product.infrastructure.interfaces.ProductDatasource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductDatasource datasource;

    @Mock
    private InventoryDatasource inventoryDatasource;

    private ProductController controller;

    @BeforeEach
    void setUp() {
        controller = new ProductController(datasource, inventoryDatasource);
    }

    @Test
    void create_shouldReturnCreatedProduct_Coxinha() {
        var createDto = new CreateProductDTO(
            "Coxinha",
            "Coxinha de frango frita",
            new BigDecimal("6.50"),
            true,
            "/images/coxinha.jpg",
            1,
            null
        );

        var id = UUID.randomUUID();
        var now = LocalDateTime.now();
        var returned = new ProductDTO(id, "Coxinha", "Coxinha de frango frita", new BigDecimal("6.50"), true, "/images/coxinha.jpg", 1, now, now);

        when(datasource.create(any())).thenReturn(returned);

        var result = controller.create(createDto);

        assertNotNull(result);
        assertEquals(id, result.id());
        assertEquals("Coxinha", result.name());

        verify(datasource, times(1)).create(any());
    }

    @Test
    void create_shouldPropagatePersistenceError_whenDatasourceFails() {
        var createDto = new CreateProductDTO(
            "Refri Lata",
            "Refrigerante cola 350ml",
            new BigDecimal("4.00"),
            true,
            "/images/refri.jpg",
            2,
            null
        );

        when(datasource.create(any())).thenThrow(new RuntimeException("DB down"));

        assertThrows(RuntimeException.class, () -> controller.create(createDto));

        verify(datasource, times(1)).create(any());
    }

    @Test
    void findAll_shouldReturnProductsByCategory_1_Bebidas() {
        var now = LocalDateTime.now();
        var list = List.of(
            new ProductDTO(UUID.randomUUID(), "Refrigerante Lata", "Refrigerante cola 350ml", new BigDecimal("4.00"), true, "/images/refri.jpg", 1, now, now),
            new ProductDTO(UUID.randomUUID(), "Suco Natural", "Suco de laranja 300ml", new BigDecimal("5.50"), true, "/images/suco.jpg", 1, now, now)
        );

        when(datasource.findAll(1)).thenReturn(list);

        var result = controller.findAll(1);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(p -> p.name().equals("Refrigerante Lata")));

        verify(datasource, times(1)).findAll(1);
    }

    @Test
    void findAll_shouldPropagate_whenDatasourceFails() {
        when(datasource.findAll(99)).thenThrow(new RuntimeException("DB down"));

        assertThrows(RuntimeException.class, () -> controller.findAll(99));

        verify(datasource, times(1)).findAll(99);
    }

    @Test
    void findAllByIds_shouldReturnProducts_matchingIds() {
        var id1 = UUID.randomUUID();
        var id2 = UUID.randomUUID();
        var now = LocalDateTime.now();
        var list = List.of(
            new ProductDTO(id1, "Coxinha", "Coxinha de frango", new BigDecimal("6.50"), true, "/images/coxinha.jpg", 2, now, now),
            new ProductDTO(id2, "Enroladinho", "Enroladinho de salsicha", new BigDecimal("5.00"), true, "/images/enroladinho.jpg", 2, now, now)
        );

        when(datasource.findAllByIds(List.of(id1, id2))).thenReturn(list);

        var result = controller.findAllByIds(List.of(id1, id2));

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(p -> p.id().equals(id1)));

        verify(datasource, times(1)).findAllByIds(List.of(id1, id2));
    }

    @Test
    void findAllByIds_shouldPropagate_whenDatasourceFails() {
        var id = UUID.randomUUID();
        when(datasource.findAllByIds(List.of(id))).thenThrow(new RuntimeException("DB down"));

        assertThrows(RuntimeException.class, () -> controller.findAllByIds(List.of(id)));

        verify(datasource, times(1)).findAllByIds(List.of(id));
    }

    @Test
    void findById_shouldReturnProduct_Coxinha() {
        var id = UUID.randomUUID();
        var now = LocalDateTime.now();
        var returned = new ProductDTO(id, "Coxinha", "Coxinha de frango frita", new BigDecimal("6.50"), true, "/images/coxinha.jpg", 1, now, now);

        when(datasource.findById(id)).thenReturn(returned);

        var result = controller.findById(id);

        assertNotNull(result);
        assertEquals(id, result.id());
        assertEquals("Coxinha", result.name());

        verify(datasource, times(1)).findById(id);
    }

    @Test
    void findById_notFound_shouldPropagateProductNotFoundException() {
        var id = UUID.randomUUID();
        when(datasource.findById(id)).thenThrow(new IllegalArgumentException("Produto com ID %s não encontrado."));

        assertThrows(IllegalArgumentException.class, () -> controller.findById(id));

        verify(datasource, times(1)).findById(id);
    }

    @Test
    void update_shouldReturnUpdatedProduct_Salvar() {
        var id = UUID.randomUUID();
        var dto = new UpdateProductDTO(id, "Salgar", "Salgado especial", new BigDecimal("7.50"), true, "/images/salgar.jpg", 3);
        var now = LocalDateTime.now();
        var returned = new ProductDTO(id, "Salgar", "Salgado especial", new BigDecimal("7.50"), true, "/images/salgar.jpg", 3, now, now);

        when(datasource.update(any())).thenReturn(returned);

        var result = controller.update(dto);

        assertNotNull(result);
        assertEquals("Salgar", result.name());

        verify(datasource, times(1)).update(any());
    }

    @Test
    void update_notFound_shouldPropagateProductNotFound() {
        var id = UUID.randomUUID();
        var dto = new UpdateProductDTO(id, "NaoExiste", "Inexistente", new BigDecimal("0.00"), true, null, 99);

        when(datasource.update(any())).thenThrow(new IllegalArgumentException("Produto com ID %s não encontrado."));

        assertThrows(IllegalArgumentException.class, () -> controller.update(dto));

        verify(datasource, times(1)).update(any());
    }

    @Test
    void delete_shouldReturnDeletedProduct() {
        var id = UUID.randomUUID();
        var now = LocalDateTime.now();
        var foundProduct = new ProductDTO(id, "Promo Coxinha", "Promoção", new BigDecimal("3.00"), true, "/images/promo.jpg", 2, now, now);
        var deletedProduct = new ProductDTO(id, "Promo Coxinha", "Promoção", new BigDecimal("3.00"), true, "/images/promo.jpg", 2, now, now);

        when(datasource.findById(id)).thenReturn(foundProduct);
        when(datasource.delete(any(UUID.class))).thenReturn(deletedProduct);

        var result = controller.delete(id);

        assertNotNull(result);
        assertEquals(id, result.id());
        assertEquals("Promo Coxinha", result.name());
        assertEquals(new BigDecimal("3.00"), result.price());

        verify(datasource, times(1)).findById(id);
        verify(datasource, times(1)).delete(any(UUID.class));
    }

    @Test
    void delete_notFound_shouldPropagateProductNotFound() {
        var id = UUID.randomUUID();

        when(datasource.findById(id)).thenThrow(new IllegalArgumentException("Produto com ID " + id + " não encontrado."));

        var exception = assertThrows(IllegalArgumentException.class, () -> controller.delete(id));
        assertTrue(exception.getMessage().contains("Produto com ID"));
        assertTrue(exception.getMessage().contains("não encontrado"));

        verify(datasource, times(1)).findById(id);
        verify(datasource, never()).delete(any());
    }
}

