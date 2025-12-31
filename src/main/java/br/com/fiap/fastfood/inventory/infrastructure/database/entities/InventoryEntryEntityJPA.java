package br.com.fiap.fastfood.inventory.infrastructure.database.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "inventory_entry")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryEntryEntityJPA {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "inventory_id", nullable = false)
    private InventoryEntityJPA inventory;

    @Column(precision = 5, scale = 2)
    private BigDecimal quantity;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "entry_date")
    private LocalDate entryDate;
}
