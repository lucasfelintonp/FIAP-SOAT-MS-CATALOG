package br.com.fiap.fastfood.inventory.infrastructure.database.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitEntityJPATest {

    private Integer unitId;
    private String unitName;
    private String unitAbbreviation;

    @BeforeEach
    void setUp() {
        unitId = 1;
        unitName = "Quilograma";
        unitAbbreviation = "KG";
    }

    @Test
    void allArgsConstructor_success_shouldCreateUnit() {
        var entity = new UnitEntityJPA(
            unitId,
            unitName,
            unitAbbreviation
        );

        assertEquals(unitId, entity.getId());
        assertEquals(unitName, entity.getName());
        assertEquals(unitAbbreviation, entity.getAbbreviation());
    }

    @Test
    void noArgsConstructor_success_shouldCreateEmptyEntity() {
        var entity = new UnitEntityJPA();

        assertNull(entity.getId());
        assertNull(entity.getName());
        assertNull(entity.getAbbreviation());
    }

    @Test
    void setters_success_shouldSetAllFields() {
        var entity = new UnitEntityJPA();

        entity.setId(unitId);
        entity.setName(unitName);
        entity.setAbbreviation(unitAbbreviation);

        assertEquals(unitId, entity.getId());
        assertEquals(unitName, entity.getName());
        assertEquals(unitAbbreviation, entity.getAbbreviation());
    }

    @Test
    void equals_success_shouldBeEqualForSameData() {
        var entity1 = new UnitEntityJPA(1, "Grama", "G");
        var entity2 = new UnitEntityJPA(1, "Grama", "G");

        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());
    }

    @Test
    void equals_error_shouldReturnFalseForDifferentIds() {
        var entity1 = new UnitEntityJPA(1, "Litro", "L");
        var entity2 = new UnitEntityJPA(2, "Litro", "L");

        assertNotEquals(entity1, entity2);
    }

    @Test
    void jpaAnnotations_success_shouldHaveCorrectTableMapping() {
        var tableAnnotation = UnitEntityJPA.class.getAnnotation(Table.class);
        assertNotNull(tableAnnotation);
        assertEquals("unit", tableAnnotation.name());
    }

    @Test
    void jpaAnnotations_success_idFieldShouldHaveIdAnnotation() throws NoSuchFieldException {
        var idField = UnitEntityJPA.class.getDeclaredField("id");
        assertTrue(idField.isAnnotationPresent(Id.class), "O campo id deve ter a anotação @Id");
    }

    @Test
    void jpaAnnotations_success_nameFieldShouldHaveCorrectConstraints() throws NoSuchFieldException {
        var nameField = UnitEntityJPA.class.getDeclaredField("name");
        var column = nameField.getAnnotation(Column.class);

        assertNotNull(column);
        assertFalse(column.nullable());
        assertEquals(50, column.length());
    }

    @Test
    void jpaAnnotations_success_abbreviationFieldShouldHaveCorrectConstraints() throws NoSuchFieldException {
        var abbreviationField = UnitEntityJPA.class.getDeclaredField("abbreviation");
        var column = abbreviationField.getAnnotation(Column.class);

        assertNotNull(column);
        assertFalse(column.nullable());
        assertEquals(10, column.length());
    }

    @Test
    void toString_success_shouldContainUnitInfo() {
        var entity = new UnitEntityJPA(1, "Unidade", "UN");

        String toString = entity.toString();
        assertTrue(toString.contains("Unidade"));
        assertTrue(toString.contains("UN"));
    }
}
