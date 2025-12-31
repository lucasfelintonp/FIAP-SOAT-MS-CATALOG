package br.com.fiap.fastfood.inventory.domain.entities;

public class UnitEntity {
    private final Integer id;
    private final String name;
    private final String abbreviation;

    public UnitEntity(Integer id, String name, String abbreviation) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("O ID não pode ser nulo ou menor ou igual a zero.");
        }

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("O Nome não pode ser nulo ou vazio.");
        }
        if (name.trim().length() > 50) {
            throw new IllegalArgumentException("O Nome não pode exceder 50 caracteres.");
        }

        if (abbreviation == null || abbreviation.trim().isEmpty()) {
            throw new IllegalArgumentException("A Abreviação não pode ser nula ou vazia.");
        }
        if (abbreviation.trim().length() > 10) {
            throw new IllegalArgumentException("A Abreviação não pode exceder 10 caracteres.");
        }

        this.id = id;
        this.name = name.trim();
        this.abbreviation = abbreviation.trim();
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }
}
