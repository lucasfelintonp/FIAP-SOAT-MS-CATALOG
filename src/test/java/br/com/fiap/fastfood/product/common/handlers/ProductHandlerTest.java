package br.com.fiap.fastfood.product.common.handlers;

import br.com.fiap.fastfood.product.application.dtos.CreateProductDTO;
import br.com.fiap.fastfood.product.application.dtos.ProductDTO;
import br.com.fiap.fastfood.product.infrastructure.interfaces.ProductDatasource;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductHandlerTest {

    @Test
    void createDelegatesToControllerAndReturns201Body() {
        ProductDatasource ds = mock(ProductDatasource.class);
        ProductHandler handler = new ProductHandler(ds);

        CreateProductDTO dto = new CreateProductDTO(
            "Cheeseburger",
            "Hambúrguer com queijo, alface e molho especial",
            new BigDecimal("19.90"),
            true,
            "/images/cheeseburger.jpg",
            1
        );
        ProductDTO returned = new ProductDTO(
            UUID.fromString("a3b5f3e0-1a2b-4c3d-8e9f-1234567890ab"),
            "Cheeseburger",
            "Hambúrguer com queijo, alface e molho especial",
            new BigDecimal("19.90"),
            true,
            "/images/cheeseburger.jpg",
            1,
            LocalDateTime.parse("2025-10-31T21:33:48.430144"),
            LocalDateTime.parse("2025-10-31T21:33:48.430144")
        );
        when(ds.create(any())).thenReturn(returned);

        var response = handler.create(dto);

        assertNotNull(response);
        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(returned.name(), response.getBody().name());
    }

    @Test
    void findAllByIdsDelegatesToControllerAndReturnsList() {
        ProductDatasource ds = mock(ProductDatasource.class);
        ProductHandler handler = new ProductHandler(ds);

        var id1 = UUID.fromString("a3b5f3e0-1a2b-4c3d-8e9f-1234567890ab");
        var id2 = UUID.fromString("b4c6d7f1-2b3c-4d5e-9f01-2345678901bc");

        ProductDTO p1 = new ProductDTO(
            id1,
            "Cheeseburger",
            "Hambúrguer com queijo, alface e molho especial",
            new BigDecimal("19.90"),
            true,
            "/images/cheeseburger.jpg",
            1,
            LocalDateTime.parse("2025-10-31T21:33:48.430144"),
            LocalDateTime.parse("2025-10-31T21:33:48.430144")
        );
        ProductDTO p2 = new ProductDTO(
            id2,
            "Chicken Sandwich",
            "Sanduíche de frango crocante com maionese e alface",
            new BigDecimal("21.50"),
            true,
            "/images/chicken_sandwich.jpg",
            1,
            LocalDateTime.parse("2025-10-31T21:33:48.430144"),
            LocalDateTime.parse("2025-10-31T21:33:48.430144")
        );

        when(ds.findAllByIds(List.of(id1, id2))).thenReturn(List.of(p1, p2));

        var response = handler.findAllByIds(List.of(id1, id2));

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(p1.name(), response.getBody().getFirst().name());
        verify(ds, times(1)).findAllByIds(List.of(id1, id2));
    }

    @Test
    void findAllByIdsReturnsEmptyWhenDatasourceEmpty() {
        ProductDatasource ds = mock(ProductDatasource.class);
        ProductHandler handler = new ProductHandler(ds);

        var id1 = UUID.fromString("a3b5f3e0-1a2b-4c3d-8e9f-1234567890ab");
        var id2 = UUID.fromString("b4c6d7f1-2b3c-4d5e-9f01-2345678901bc");

        when(ds.findAllByIds(List.of(id1, id2))).thenReturn(List.of());

        var response = handler.findAllByIds(List.of(id1, id2));

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().size());
        verify(ds, times(1)).findAllByIds(List.of(id1, id2));
    }
}
