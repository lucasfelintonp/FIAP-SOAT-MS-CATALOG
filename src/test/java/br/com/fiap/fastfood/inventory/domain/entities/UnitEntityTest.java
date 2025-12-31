package br.com.fiap.fastfood.inventory.domain.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class UnitEntityTest {

    @Test
    @DisplayName("Deve criar uma unidade de medida com sucesso quando os dados são válidos")
    void constructor_success_shouldCreateEntity() {
        Integer id = 1;
        String name = " Quilograma ";
        String abbreviation = " KG ";

        UnitEntity entity = new UnitEntity(id, name, abbreviation);

        assertEquals(1, entity.getId());
        assertEquals("Quilograma", entity.getName());
        assertEquals("KG", entity.getAbbreviation());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    @DisplayName("Deve lançar exceção quando o ID for zero ou negativo")
    void constructor_error_invalidId_shouldThrowException(int invalidId) {
        assertThrows(IllegalArgumentException.class, () ->
            new UnitEntity(invalidId, "Litro", "L")
        );
    }

    @Test
    @DisplayName("Deve lançar exceção quando o ID for nulo")
    void constructor_error_nullId_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () ->
            new UnitEntity(null, "Litro", "L")
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("Deve lançar exceção quando o nome for nulo, vazio ou apenas espaços")
    void constructor_error_invalidName_shouldThrowException(String invalidName) {
        assertThrows(IllegalArgumentException.class, () ->
            new UnitEntity(1, invalidName, "UN")
        );
    }

    @Test
    @DisplayName("Deve lançar exceção quando o nome exceder 50 caracteres")
    void constructor_error_nameTooLong_shouldThrowException() {
        String longName = "a".repeat(51);
        assertThrows(IllegalArgumentException.class, () ->
            new UnitEntity(1, longName, "UN")
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("Deve lançar exceção quando a abreviação for nula ou vazia")
    void constructor_error_invalidAbbreviation_shouldThrowException(String invalidAbbr) {
        assertThrows(IllegalArgumentException.class, () ->
            new UnitEntity(1, "Unidade", invalidAbbr)
        );
    }

    @Test
    @DisplayName("Deve lançar exceção quando a abreviação exceder 10 caracteres")
    void constructor_error_abbreviationTooLong_shouldThrowException() {
        String longAbbr = "ABCDEFGHIJK";
        assertThrows(IllegalArgumentException.class, () ->
            new UnitEntity(1, "Unidade", longAbbr)
        );
    }
}
