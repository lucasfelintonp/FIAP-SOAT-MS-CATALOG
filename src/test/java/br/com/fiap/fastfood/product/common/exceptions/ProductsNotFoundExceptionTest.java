package br.com.fiap.fastfood.product.common.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductsNotFoundExceptionTest {

    @Test
    void messageIsPreserved() {
        var ex = new ProductsNotFoundException("m");
        assertEquals("m", ex.getMessage());
    }
}
