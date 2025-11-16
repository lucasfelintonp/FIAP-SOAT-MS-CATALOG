package br.com.fiap.fastfood.category.common.exceptions;

public class ProductCategoryInvalidRequestException extends RuntimeException {
    public ProductCategoryInvalidRequestException(String message) {
        super(message);
    }
}
