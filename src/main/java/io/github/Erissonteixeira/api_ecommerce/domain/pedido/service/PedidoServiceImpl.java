package io.github.Erissonteixeira.api_ecommerce.domain.pedido.service;

import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity.CarrinhoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity.ItemCarrinhoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.repository.CarrinhoRepository;
import io.github.Erissonteixeira.api_ecommerce.domain.pedido.entity.PedidoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.pedido.entity.PedidoItemEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.pedido.repository.PedidoRepository;
import io.github.Erissonteixeira.api_ecommerce.exception.RecursoNaoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final CarrinhoRepository carrinhoRepository;
    private final PedidoRepository pedidoRepository;

    public PedidoServiceImpl(CarrinhoRepository carrinhoRepository, PedidoRepository pedidoRepository) {
        this.carrinhoRepository = carrinhoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    @Transactional
    public PedidoEntity criarPedidoDoCarrinho(Long carrinhoId) {

        CarrinhoEntity carrinho = carrinhoRepository.buscarComItensPorId(carrinhoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Carrinho nao encontrado."));

        PedidoEntity pedido = new PedidoEntity();

        for (ItemCarrinhoEntity item : carrinho.getItens()) {
            PedidoItemEntity pedidoItem = new PedidoItemEntity(
                    item.getProdutoId(),
                    item.getNomeProduto(),
                    item.getPrecoUnitario(),
                    item.getQuantidade()
            );
            pedido.adicionarItem(pedidoItem);
        }

        return pedidoRepository.save(pedido);
    }
}
