package io.github.Erissonteixeira.api_ecommerce.domain.pedido.service;

import io.github.Erissonteixeira.api_ecommerce.domain.pedido.entity.PedidoEntity;

public interface PedidoService {
    PedidoEntity criarPedidoDoCarrinho(Long carrinhoId);
}
