package br.com.fiap.fastfood.product.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductsConstantsTest {

    @Test
    void constantsNotEmpty() {
        assertNotNull(ProductsConstants.PRODUCT_INVALID_NAME);
        assertFalse(ProductsConstants.PRODUCT_INVALID_NAME.isEmpty());
    }
}
