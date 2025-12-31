package br.com.fiap.fastfood.inventory.domain.use_cases;

import br.com.fiap.fastfood.inventory.application.gateways.InventoryGateway;
import br.com.fiap.fastfood.inventory.domain.entities.InventoryEntity;
import br.com.fiap.fastfood.inventory.domain.entities.InventoryEntryEntity;
import br.com.fiap.fastfood.inventory.domain.entities.InventoryProductsEntity;
import br.com.fiap.fastfood.inventory.domain.entities.UnitEntity;
import br.com.fiap.fastfood.product.application.gateways.ProductGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

class EnableElegibleProductsUseCaseTest {

    private InventoryGateway inventoryGateway;
    private ProductGateway productGateway;
    private EnableElegibleProductsUseCase useCase;
    private UnitEntity unit;

    @BeforeEach
    void setUp() {
        inventoryGateway = mock(InventoryGateway.class);
        productGateway = mock(ProductGateway.class);
        useCase = new EnableElegibleProductsUseCase(inventoryGateway, productGateway);
        unit = new UnitEntity(1, "Unidade", "UN");
    }

    @Test
    void shouldEnableProductWhenAllIngredientsAreAboveMinimum() {
        UUID productId = UUID.randomUUID();

        InventoryEntity breadInv = new InventoryEntity(UUID.randomUUID(), "Pão", unit,
            new BigDecimal("10.00"), new BigDecimal("5.00"), "", null, null);

        InventoryEntryEntity entry = new InventoryEntryEntity(UUID.randomUUID(), breadInv,
            new BigDecimal("5.00"), LocalDate.now(), LocalDate.now());

        InventoryProductsEntity breadRelation = new InventoryProductsEntity(UUID.randomUUID(), productId, breadInv, BigDecimal.ONE, null, null);

        when(inventoryGateway.getInventoryProductByInventoryId(breadInv.getId())).thenReturn(List.of(breadRelation));

        when(inventoryGateway.getInventoryProductByProductId(productId)).thenReturn(List.of(breadRelation));

        useCase.run(entry);

        verify(productGateway, times(1)).enableProduct(productId);
    }

    @Test
    void shouldNotEnableProductWhenAtLeastOneIngredientIsBelowMinimum() {
        UUID productId = UUID.randomUUID();

        InventoryEntity breadInv = new InventoryEntity(UUID.randomUUID(), "Pão", unit,
            new BigDecimal("10.00"), new BigDecimal("5.00"), "", null, null);

        InventoryEntity meatInv = new InventoryEntity(UUID.randomUUID(), "Carne", unit,
            new BigDecimal("2.00"), new BigDecimal("5.00"), "", null, null);

        InventoryEntryEntity entry = new InventoryEntryEntity(UUID.randomUUID(), breadInv,
            new BigDecimal("5.00"), LocalDate.now(), LocalDate.now());

        InventoryProductsEntity rel1 = new InventoryProductsEntity(UUID.randomUUID(), productId, breadInv, BigDecimal.ONE, null, null);
        InventoryProductsEntity rel2 = new InventoryProductsEntity(UUID.randomUUID(), productId, meatInv, BigDecimal.ONE, null, null);

        when(inventoryGateway.getInventoryProductByInventoryId(breadInv.getId())).thenReturn(List.of(rel1));

        when(inventoryGateway.getInventoryProductByProductId(productId)).thenReturn(List.of(rel1, rel2));

        useCase.run(entry);

        verify(productGateway, never()).enableProduct(productId);
    }
}
