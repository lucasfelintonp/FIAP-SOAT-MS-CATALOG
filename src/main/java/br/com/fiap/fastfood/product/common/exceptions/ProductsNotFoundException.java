package br.com.fiap.fastfood.product.common.exceptions;

public class ProductsNotFoundException extends RuntimeException {
    public ProductsNotFoundException(String message) {
        super(message);
    }
}
