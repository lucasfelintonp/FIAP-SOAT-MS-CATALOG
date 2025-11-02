package br.com.fiap.fastfood.products.common.exceptions;

public class ProductMappingException extends RuntimeException {
    public ProductMappingException(String message, Throwable cause) {
        super(message, cause);
    }
}
