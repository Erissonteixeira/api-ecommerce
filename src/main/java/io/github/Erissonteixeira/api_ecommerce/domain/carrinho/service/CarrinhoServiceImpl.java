package io.github.Erissonteixeira.api_ecommerce.domain.carrinho.service;

import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity.CarrinhoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity.ItemCarrinhoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.repository.CarrinhoRepository;
import io.github.Erissonteixeira.api_ecommerce.exception.NegocioException;
import io.github.Erissonteixeira.api_ecommerce.exception.RecursoNaoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional(readOnly = true)
public class CarrinhoServiceImpl implements CarrinhoService {

    private final CarrinhoRepository carrinhoRepository;

    public CarrinhoServiceImpl(CarrinhoRepository carrinhoRepository) {
        this.carrinhoRepository = carrinhoRepository;
    }

    @Override
    @Transactional
    public CarrinhoEntity criarCarrinho() {
        CarrinhoEntity carrinho = new CarrinhoEntity();
        return carrinhoRepository.save(carrinho);
    }

    @Override
    @Transactional
    public CarrinhoEntity adicionarItem(
            Long carrinhoId,
            Long produtoId,
            String nomeProduto,
            BigDecimal preco,
            Integer quantidade
    ) {
        validarAdicionarItem(carrinhoId, produtoId, nomeProduto, preco, quantidade);

        CarrinhoEntity carrinho = buscarPorId(carrinhoId);

        carrinho.adicionarItem(
                new ItemCarrinhoEntity(produtoId, nomeProduto.trim(), preco, quantidade)
        );

        return carrinhoRepository.save(carrinho);
    }

    @Override
    @Transactional
    public CarrinhoEntity removerItem(Long carrinhoId, Long produtoId) {
        if (carrinhoId == null) {
            throw new NegocioException("CarrinhoId não pode ser nulo");
        }
        if (produtoId == null) {
            throw new NegocioException("ProdutoId não pode ser nulo");
        }

        CarrinhoEntity carrinho = buscarPorId(carrinhoId);
        carrinho.removerItem(produtoId);

        return carrinhoRepository.save(carrinho);
    }

    @Override
    public BigDecimal calcularTotal(Long carrinhoId) {
        if (carrinhoId == null) {
            throw new NegocioException("CarrinhoId não pode ser nulo");
        }

        CarrinhoEntity carrinho = buscarPorId(carrinhoId);
        return carrinho.getTotal();
    }

    @Override
    @Transactional(readOnly = true)
    public CarrinhoEntity buscarPorId(Long carrinhoId) {
        if (carrinhoId == null) {
            throw new NegocioException("CarrinhoId não pode ser nulo");
        }

        return carrinhoRepository.buscarComItensPorId(carrinhoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Carrinho não encontrado"));
    }

    private void validarAdicionarItem(
            Long carrinhoId,
            Long produtoId,
            String nomeProduto,
            BigDecimal preco,
            Integer quantidade
    ) {
        if (carrinhoId == null) {
            throw new NegocioException("CarrinhoId não pode ser nulo");
        }
        if (produtoId == null) {
            throw new NegocioException("ProdutoId não pode ser nulo");
        }
        if (nomeProduto == null || nomeProduto.trim().isEmpty()) {
            throw new NegocioException("Nome do produto é obrigatório");
        }
        if (preco == null) {
            throw new NegocioException("Preço é obrigatório");
        }
        if (preco.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegocioException("Preço não pode ser negativo");
        }
        if (quantidade == null || quantidade <= 0) {
            throw new NegocioException("Quantidade deve ser maior que zero");
        }
    }
}
