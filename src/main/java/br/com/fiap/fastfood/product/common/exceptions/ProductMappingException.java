package br.com.fiap.fastfood.product.common.exceptions;

public class ProductMappingException extends RuntimeException {
    public ProductMappingException(String message, Throwable cause) {
        super(message, cause);
    }
}
