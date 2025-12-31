package br.com.fiap.fastfood.inventory.domain.use_cases;

import br.com.fiap.fastfood.inventory.application.dtos.ProductsQuantityDTO;
import br.com.fiap.fastfood.inventory.application.gateways.InventoryGateway;
import br.com.fiap.fastfood.inventory.application.gateways.InventoryProductGateway;
import br.com.fiap.fastfood.inventory.common.exceptions.InvalidQuantityException;
import br.com.fiap.fastfood.inventory.domain.entities.InventoryEntity;
import br.com.fiap.fastfood.inventory.domain.entities.InventoryProductsEntity;
import br.com.fiap.fastfood.inventory.domain.entities.UnitEntity;
import br.com.fiap.fastfood.product.application.gateways.ProductGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DiscountInventoryItemsByProductsUseCaseTest {

    private InventoryProductGateway gateway;
    private InventoryGateway inventoryGateway;
    private ProductGateway productGateway;
    private DiscountInventoryItemsByProductsUseCase useCase;

    @BeforeEach
    void setUp() {
        gateway = mock(InventoryProductGateway.class);
        inventoryGateway = mock(InventoryGateway.class);
        productGateway = mock(ProductGateway.class);
        useCase = new DiscountInventoryItemsByProductsUseCase(gateway, inventoryGateway, productGateway);
    }

    @Test
    void shouldDiscountInventoryCorrectly() {
        UUID productId = UUID.randomUUID();
        UUID inventoryId = UUID.randomUUID();
        UnitEntity unit = new UnitEntity(1, "Grama", "G");

        InventoryEntity inventory = new InventoryEntity(inventoryId, "Carne", unit, new BigDecimal("100.00"),
            new BigDecimal("10.00"), "", null, null);

        InventoryProductsEntity relation = new InventoryProductsEntity(UUID.randomUUID(), productId, inventory, new BigDecimal("20.00"), null, null);

        ProductsQuantityDTO sale = new ProductsQuantityDTO(productId, new BigDecimal("2.00"));

        when(gateway.getInventoryProductByProductId(productId)).thenReturn(List.of(relation));

        useCase.run(List.of(sale));

        assertTrue(new BigDecimal("60.00").compareTo(inventory.getQuantity()) == 0,
            "O saldo final deve ser 60.00");

        verify(inventoryGateway, times(1)).update(inventory);
    }

    @Test
    void shouldThrowExceptionWhenStockIsInsufficient() {
        UUID productId = UUID.randomUUID();
        InventoryEntity inventory = new InventoryEntity(UUID.randomUUID(), "Queijo", new UnitEntity(1, "Un", "U"),
            new BigDecimal("1.00"), BigDecimal.ZERO, "", null, null);

        InventoryProductsEntity relation = new InventoryProductsEntity(UUID.randomUUID(), productId, inventory, new BigDecimal("2.00"), null, null);
        ProductsQuantityDTO sale = new ProductsQuantityDTO(productId, new BigDecimal("1.00"));

        when(gateway.getInventoryProductByProductId(productId)).thenReturn(List.of(relation));

        InvalidQuantityException ex = assertThrows(InvalidQuantityException.class, () -> useCase.run(List.of(sale)));
        assertTrue(ex.getMessage().contains("Estoque insuficiente"));
        verify(inventoryGateway, never()).update(any());
    }

    @Test
    void shouldDisableProductWhenStockReachesZero() {
        UUID productId = UUID.randomUUID();
        UUID inventoryId = UUID.randomUUID();

        InventoryEntity inventory = new InventoryEntity(inventoryId, "Pão", new UnitEntity(1, "Un", "U"),
            new BigDecimal("1.00"), BigDecimal.ZERO, "", null, null);

        InventoryProductsEntity relation = new InventoryProductsEntity(UUID.randomUUID(), productId, inventory, new BigDecimal("1.00"), null, null);
        ProductsQuantityDTO sale = new ProductsQuantityDTO(productId, new BigDecimal("1.00"));

        when(gateway.getInventoryProductByProductId(productId)).thenReturn(List.of(relation));
        when(gateway.getInventoryProductByInventoryId(inventoryId)).thenReturn(List.of(relation));

        useCase.run(List.of(sale));

        assertTrue(BigDecimal.ZERO.compareTo(inventory.getQuantity()) == 0, "O estoque deve ser exatamente ZERO");
        verify(productGateway, times(1)).disableProduct(productId);
    }

    @Test
    void shouldThrowExceptionForInvalidInputQuantity() {
        ProductsQuantityDTO sale = new ProductsQuantityDTO(UUID.randomUUID(), new BigDecimal("-5.00"));

        assertThrows(InvalidQuantityException.class, () -> useCase.run(List.of(sale)));
    }
}
