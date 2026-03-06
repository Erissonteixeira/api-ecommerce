package io.github.Erissonteixeira.api_ecommerce.domain.pedido.service;

import io.github.Erissonteixeira.api_ecommerce.domain.pedido.entity.PedidoEntity;

import java.util.List;

public interface PedidoService {

    PedidoEntity criarPedidoDoCarrinho(String email);

    List<PedidoEntity> listarMeusPedidos(String email);

    PedidoEntity buscarMeuPedidoPorId(String email, Long pedidoId);
}