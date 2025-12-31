package br.com.fiap.fastfood.inventory.common.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EntityNotFoundExceptionTest {

    @Test
    void messageIsPreserved() {
        String expectedMessage = "Item de estoque não encontrado";
        var ex = new EntityNotFoundException(expectedMessage);

        assertEquals(expectedMessage, ex.getMessage());
    }
}
