package br.com.fiap.fastfood.product.common.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductInvalidRequestExceptionTest {

    @Test
    void messageIsPreserved() {
        var ex = new ProductInvalidRequestException("msg");
        assertEquals("msg", ex.getMessage());
    }
}
