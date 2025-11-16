package br.com.fiap.fastfood.category.infrastructure.database.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "product_category")
@Table(name = "product_categories")
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryEntityJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    public ProductCategoryEntityJPA(String name) {
        this.name = name;
    }
}
