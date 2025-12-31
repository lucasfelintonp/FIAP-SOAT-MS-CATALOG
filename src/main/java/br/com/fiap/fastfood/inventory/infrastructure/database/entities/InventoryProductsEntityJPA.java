package br.com.fiap.fastfood.inventory.infrastructure.database.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "inventory_products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryProductsEntityJPA {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    private UUID productId;

    @ManyToOne
    @JoinColumn(name = "inventory_id")
    private InventoryEntityJPA inventory;

    private BigDecimal quantity;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
