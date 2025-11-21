package br.com.fiap.fastfood.product.common.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductPersistenceExceptionTest {

    @Test
    void messageAndCause() {
        Exception cause = new Exception("c");
        var ex = new ProductPersistenceException("m", cause);
        assertEquals("m", ex.getMessage());
        assertEquals(cause, ex.getCause());
    }
}
