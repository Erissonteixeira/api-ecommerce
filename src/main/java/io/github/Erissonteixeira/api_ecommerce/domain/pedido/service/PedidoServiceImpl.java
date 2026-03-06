package io.github.Erissonteixeira.api_ecommerce.domain.pedido.service;

import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity.CarrinhoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity.ItemCarrinhoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.repository.CarrinhoRepository;
import io.github.Erissonteixeira.api_ecommerce.domain.pedido.entity.PedidoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.pedido.entity.PedidoItemEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.pedido.repository.PedidoRepository;
import io.github.Erissonteixeira.api_ecommerce.domain.usuario.entity.UsuarioEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.usuario.repository.UsuarioRepository;
import io.github.Erissonteixeira.api_ecommerce.exception.NegocioException;
import io.github.Erissonteixeira.api_ecommerce.exception.RecursoNaoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final CarrinhoRepository carrinhoRepository;
    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;

    public PedidoServiceImpl(
            CarrinhoRepository carrinhoRepository,
            PedidoRepository pedidoRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.carrinhoRepository = carrinhoRepository;
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public PedidoEntity criarPedidoDoCarrinho(String email) {

        if (email == null || email.trim().isEmpty()) {
            throw new NegocioException("Email inválido");
        }

        String normalized = email.trim().toLowerCase();

        UsuarioEntity usuario = usuarioRepository.findByEmail(normalized)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        CarrinhoEntity carrinho = carrinhoRepository.buscarPorUsuarioIdComItens(usuario.getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Carrinho não encontrado"));

        if (carrinho.getItens().isEmpty()) {
            throw new NegocioException("Não é possível gerar pedido com carrinho vazio");
        }

        PedidoEntity pedido = new PedidoEntity();
        pedido.setUsuario(usuario);

        for (ItemCarrinhoEntity item : carrinho.getItens()) {
            PedidoItemEntity pedidoItem = new PedidoItemEntity(
                    item.getProdutoId(),
                    item.getNomeProduto(),
                    item.getPrecoUnitario(),
                    item.getQuantidade()
            );
            pedido.adicionarItem(pedidoItem);
        }

        PedidoEntity salvo = pedidoRepository.save(pedido);

        carrinho.limpar();
        carrinhoRepository.save(carrinho);

        return salvo;
    }
}