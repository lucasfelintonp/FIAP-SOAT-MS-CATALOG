package br.com.fiap.fastfood.inventory.common.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InvalidQuantityExceptionTest {

    @Test
    void messageIsPreserved() {
        String expectedMessage = "Quantidade inválida para o item";
        var ex = new InvalidQuantityException(expectedMessage);

        assertEquals(expectedMessage, ex.getMessage());
    }
}
