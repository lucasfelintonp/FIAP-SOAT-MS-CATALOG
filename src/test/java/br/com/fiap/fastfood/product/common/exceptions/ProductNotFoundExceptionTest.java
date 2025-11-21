package br.com.fiap.fastfood.product.common.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductNotFoundExceptionTest {

    @Test
    void messageIsPreserved() {
        var ex = new ProductNotFoundException("m");
        assertEquals("m", ex.getMessage());
    }
}
