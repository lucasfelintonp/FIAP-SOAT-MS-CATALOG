package br.com.fiap.fastfood.product.common;

public class ProductsConstants {

    public static final String PRODUCT_CATEGORY_ERROR_CREATE = "Erro inesperado ao criar a categoria.";
    public static final String PRODUCT_CATEGORY_INVALID_NAME = "O nome da categoria deve ter no mínimo 2 caracteres e não pode estar em branco.";
    public static final String PRODUCT_CATEGORY_NAME_INVALID = "O nome da categoria não pode ser nulo ou em branco.";
    public static final String PRODUCT_CATEGORY_NOT_FOUND = "Categoria de produto com ID %d não encontrada.";
    public static final String PRODUCT_CATEGORY_REQUEST_NULL = "O corpo da requisição não pode ser nulo.";

    public static final String PRODUCT_ERROR_MAPPING = "Erro ao converter o DTO para entidade de produto.";
    public static final String PRODUCT_ERROR_SAVING = "Erro ao salvar o produto no banco de dados.";
    public static final String PRODUCT_INVALID_CATEGORY = "A categoria do produto é obrigatória.";
    public static final String PRODUCT_INVALID_DESCRIPTION = "A descrição do produto é obrigatória.";
    public static final String PRODUCT_INVALID_IS_ACTIVE = "O status de ativo/inativo do produto é obrigatório.";
    public static final String PRODUCT_INVALID_NAME = "O nome do produto é obrigatório.";
    public static final String PRODUCT_INVALID_PRICE = "O preço do produto deve ser maior que zero.";
    public static final String PRODUCT_NOT_FOUND = "Produto com ID %s não encontrado.";
    public static final String PRODUCTS_NOT_FOUND = "Nenhum produto foi encontrado.";

    private ProductsConstants() {
    }
}
