package io.github.Erissonteixeira.api_ecommerce.domain.carrinho.service;

import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity.CarrinhoEntity;

import java.math.BigDecimal;

public interface CarrinhoService {

    CarrinhoEntity criarCarrinho();

    CarrinhoEntity adicionarItem(
            Long carrinhoId,
            Long produtoId,
            String nomeProduto,
            BigDecimal preco,
            Integer quantidade
    );

    CarrinhoEntity removerItem(
            Long carrinhoId,
            Long produtoId
    );

    BigDecimal calcularTotal(Long carrinhoId);

    CarrinhoEntity buscarPorId(Long carrinhoId);
}
