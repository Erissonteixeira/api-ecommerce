package io.github.Erissonteixeira.api_ecommerce.domain.carrinho.service;

import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity.CarrinhoEntity;

public interface CarrinhoService {

    CarrinhoEntity obterOuCriarCarrinho(String email);

    CarrinhoEntity adicionarItem(String email, Long produtoId, Integer quantidade);

    CarrinhoEntity removerItem(String email, Long produtoId);

    void limpar(String email);
}