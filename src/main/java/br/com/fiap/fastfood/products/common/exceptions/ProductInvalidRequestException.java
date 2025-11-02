package br.com.fiap.fastfood.products.common.exceptions;

public class ProductInvalidRequestException extends RuntimeException {
    public ProductInvalidRequestException(String message) {
        super(message);
    }
}
