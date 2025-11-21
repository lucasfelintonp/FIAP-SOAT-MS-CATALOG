package br.com.fiap.fastfood.product.common.handlers;

import br.com.fiap.fastfood.product.application.dtos.CreateProductDTO;
import br.com.fiap.fastfood.product.application.dtos.ProductDTO;
import br.com.fiap.fastfood.product.infrastructure.interfaces.ProductDatasource;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
}
