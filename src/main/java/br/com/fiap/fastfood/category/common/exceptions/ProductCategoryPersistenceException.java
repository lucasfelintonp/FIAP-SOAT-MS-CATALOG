package br.com.fiap.fastfood.category.common.exceptions;

public class ProductCategoryPersistenceException extends RuntimeException {
    public ProductCategoryPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
