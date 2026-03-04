package io.github.Erissonteixeira.api_ecommerce.domain.carrinho.service;

import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity.CarrinhoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity.ItemCarrinhoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.repository.CarrinhoRepository;
import io.github.Erissonteixeira.api_ecommerce.domain.produto.entity.ProdutoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.produto.repository.ProdutoRepository;
import io.github.Erissonteixeira.api_ecommerce.domain.usuario.entity.UsuarioEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.usuario.repository.UsuarioRepository;
import io.github.Erissonteixeira.api_ecommerce.exception.NegocioException;
import io.github.Erissonteixeira.api_ecommerce.exception.RecursoNaoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CarrinhoServiceImpl implements CarrinhoService {

    private final CarrinhoRepository carrinhoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProdutoRepository produtoRepository;

    public CarrinhoServiceImpl(
            CarrinhoRepository carrinhoRepository,
            UsuarioRepository usuarioRepository,
            ProdutoRepository produtoRepository
    ) {
        this.carrinhoRepository = carrinhoRepository;
        this.usuarioRepository = usuarioRepository;
        this.produtoRepository = produtoRepository;
    }

    @Override
    @Transactional
    public CarrinhoEntity obterOuCriarCarrinho(String email) {
        UsuarioEntity usuario = buscarUsuarioPorEmail(email);

        return carrinhoRepository.buscarPorUsuarioIdComItens(usuario.getId())
                .orElseGet(() -> carrinhoRepository.save(new CarrinhoEntity(usuario)));
    }

    @Override
    @Transactional
    public CarrinhoEntity adicionarItem(String email, Long produtoId, Integer quantidade) {
        if (produtoId == null) throw new NegocioException("ProdutoId não pode ser nulo");
        if (quantidade == null || quantidade <= 0) throw new NegocioException("Quantidade deve ser maior que zero");

        CarrinhoEntity carrinho = obterOuCriarCarrinho(email);

        ProdutoEntity produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado"));

        if (Boolean.FALSE.equals(produto.getAtivo())) {
            throw new NegocioException("Produto indisponível");
        }

        carrinho.adicionarItem(new ItemCarrinhoEntity(
                produto.getId(),
                produto.getNome(),
                produto.getPreco(),
                quantidade
        ));

        return carrinhoRepository.save(carrinho);
    }

    @Override
    @Transactional
    public CarrinhoEntity removerItem(String email, Long produtoId) {
        if (produtoId == null) throw new NegocioException("ProdutoId não pode ser nulo");

        CarrinhoEntity carrinho = obterOuCriarCarrinho(email);
        carrinho.removerItem(produtoId);

        return carrinhoRepository.save(carrinho);
    }

    @Override
    @Transactional
    public void limpar(String email) {
        CarrinhoEntity carrinho = obterOuCriarCarrinho(email);
        carrinho.limpar();
        carrinhoRepository.save(carrinho);
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