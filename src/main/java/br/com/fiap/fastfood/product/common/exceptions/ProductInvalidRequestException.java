package br.com.fiap.fastfood.product.common.exceptions;

public class ProductInvalidRequestException extends RuntimeException {
    public ProductInvalidRequestException(String message) {
        super(message);
    }
}
