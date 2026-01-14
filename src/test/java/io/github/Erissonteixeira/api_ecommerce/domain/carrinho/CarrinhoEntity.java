package io.github.Erissonteixeira.api_ecommerce.domain.carrinho;

import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity.CarrinhoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity.ItemCarrinhoEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

}
