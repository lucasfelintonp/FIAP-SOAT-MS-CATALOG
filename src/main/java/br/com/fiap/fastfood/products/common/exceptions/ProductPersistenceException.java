package br.com.fiap.fastfood.products.common.exceptions;

public class ProductPersistenceException extends RuntimeException {
    public ProductPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
