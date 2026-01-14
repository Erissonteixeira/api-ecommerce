package io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CarrinhoEntityTest {

    @Test
    void deveAdicionarNovoItemAoCarrinho() {
        CarrinhoEntity carrinho = new CarrinhoEntity();

        carrinho.adicionarItem(
                new ItemCarrinhoEntity(
                        1L,
                        "Produto A",
                        new BigDecimal("50.00"),
                        1
                )
        );

        assertEquals(1, carrinho.getItens().size());
    }

    @Test
    void deveIncrementarQuantidadeSeProdutoJaExistir() {
        CarrinhoEntity carrinho = new CarrinhoEntity();

        carrinho.adicionarItem(new ItemCarrinhoEntity(
                1L, "Produto A", new BigDecimal("50.00"), 1
        ));

        carrinho.adicionarItem(new ItemCarrinhoEntity(
                1L, "Produto A", new BigDecimal("50.00"), 2
        ));

        assertEquals(3, carrinho.getItens().get(0).getQuantidade());
    }

    @Test
    void deveCalcularTotalDoCarrinho() {
        CarrinhoEntity carrinho = new CarrinhoEntity();

        carrinho.adicionarItem(new ItemCarrinhoEntity(
                1L, "Produto A", new BigDecimal("10.00"), 2
        ));

        carrinho.adicionarItem(new ItemCarrinhoEntity(
                2L, "Produto B", new BigDecimal("20.00"), 1
        ));

        BigDecimal total = carrinho.calcularTotal();

        assertEquals(new BigDecimal("40.00"), total);
    }

    @Test
    void deveRemoverItemQuandoQuantidadeForUm() {
        CarrinhoEntity carrinho = new CarrinhoEntity();

        carrinho.adicionarItem(new ItemCarrinhoEntity(
                1L, "Produto A", new BigDecimal("10.00"), 1
        ));

        carrinho.removerItem(1L);

        assertEquals(0, carrinho.getItens().size());
    }

    @Test
    void deveLancarErroAoRemoverProdutoInexistente() {
        CarrinhoEntity carrinho = new CarrinhoEntity();

        assertThrows(
                RuntimeException.class,
                () -> carrinho.removerItem(99L)
        );
    }
}
