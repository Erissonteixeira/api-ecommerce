package io.github.Erissonteixeira.api_ecommerce.domain.pedido.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PedidoEntityTest {

    @Test
    void deveIniciarComTotalZeroEListaVazia() {
        PedidoEntity pedido = new PedidoEntity();

        assertEquals(BigDecimal.ZERO, pedido.getTotal());
        assertNotNull(pedido.getItens());
        assertTrue(pedido.getItens().isEmpty());
    }

    @Test
    void adicionarItem_deveSetarPedidoNoItemERecalcularTotal() {
        PedidoEntity pedido = new PedidoEntity();

        PedidoItemEntity item1 = new PedidoItemEntity(10L, "mouse gamer", new BigDecimal("150.00"), 2); // 300
        PedidoItemEntity item2 = new PedidoItemEntity(11L, "teclado", new BigDecimal("200.00"), 1);     // 200

        pedido.adicionarItem(item1);
        pedido.adicionarItem(item2);

        assertEquals(2, pedido.getItens().size());
        assertEquals(new BigDecimal("500.00"), pedido.getTotal());
    }

    @Test
    void removerItem_deveRemoverERecalcularTotal() {
        PedidoEntity pedido = new PedidoEntity();

        PedidoItemEntity item1 = new PedidoItemEntity(10L, "mouse gamer", new BigDecimal("150.00"), 2); // 300
        PedidoItemEntity item2 = new PedidoItemEntity(11L, "teclado", new BigDecimal("200.00"), 1);     // 200

        pedido.adicionarItem(item1);
        pedido.adicionarItem(item2);

        assertEquals(new BigDecimal("500.00"), pedido.getTotal());

        pedido.removerItem(item1);

        assertEquals(1, pedido.getItens().size());
        assertEquals(new BigDecimal("200.00"), pedido.getTotal());
    }

    @Test
    void adicionarItem_quandoNulo_naoDeveExplodirNemAlterarTotal() {
        PedidoEntity pedido = new PedidoEntity();
        pedido.adicionarItem(null);

        assertTrue(pedido.getItens().isEmpty());
        assertEquals(BigDecimal.ZERO, pedido.getTotal());
    }

    @Test
    void removerItem_quandoNulo_naoDeveExplodir() {
        PedidoEntity pedido = new PedidoEntity();
        pedido.removerItem(null);

        assertTrue(pedido.getItens().isEmpty());
        assertEquals(BigDecimal.ZERO, pedido.getTotal());
    }
}
