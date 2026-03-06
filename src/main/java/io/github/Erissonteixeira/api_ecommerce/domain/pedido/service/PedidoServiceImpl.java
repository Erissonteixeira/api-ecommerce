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

import java.util.List;

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

        UsuarioEntity usuario = buscarUsuarioPorEmail(email);

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

    @Override
    @Transactional(readOnly = true)
    public List<PedidoEntity> listarMeusPedidos(String email) {
        UsuarioEntity usuario = buscarUsuarioPorEmail(email);
        return pedidoRepository.findAllByUsuarioIdOrderByCriadoEmDesc(usuario.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public PedidoEntity buscarMeuPedidoPorId(String email, Long pedidoId) {
        if (pedidoId == null) {
            throw new NegocioException("Id do pedido é obrigatório");
        }

        UsuarioEntity usuario = buscarUsuarioPorEmail(email);

        return pedidoRepository.findByIdAndUsuarioId(pedidoId, usuario.getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado"));
    }

    private UsuarioEntity buscarUsuarioPorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new NegocioException("Email inválido");
        }

        String normalized = email.trim().toLowerCase();

        return usuarioRepository.findByEmail(normalized)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));
    }
}