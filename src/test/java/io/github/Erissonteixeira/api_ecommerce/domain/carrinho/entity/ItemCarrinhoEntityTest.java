package io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemCarrinhoEntityTest {

    @Test
    void deveCalcularSubtotalCorretamente() {
        ItemCarrinhoEntity item = new ItemCarrinhoEntity(
                1L,
                "Produto Teste",
                new BigDecimal("10.00"),
                3
        );

        BigDecimal subtotal = item.calcularSubtotal();

        assertEquals(new BigDecimal("30.00"), subtotal);
    }
}
