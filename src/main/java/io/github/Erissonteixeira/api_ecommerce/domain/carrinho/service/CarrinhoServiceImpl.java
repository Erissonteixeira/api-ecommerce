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
@Transactional
public class CarrinhoServiceImpl implements CarrinhoService {

    private final CarrinhoRepository carrinhoRepository;

    public CarrinhoServiceImpl(CarrinhoRepository carrinhoRepository) {
        this.carrinhoRepository = carrinhoRepository;
    }

    @Override
    public CarrinhoEntity criarCarrinho() {
        CarrinhoEntity carrinho = new CarrinhoEntity();
        return carrinhoRepository.save(carrinho);
    }

    @Override
    public CarrinhoEntity adicionarItem(
            Long carrinhoId,
            Long produtoId,
            String nomeProduto,
            BigDecimal preco,
            Integer quantidade
    ) {
        CarrinhoEntity carrinho = buscarPorId(carrinhoId);

        if (quantidade <= 0) {
            throw new NegocioException("Quantidade deve ser maior que zero");
        }

        carrinho.adicionarItem(
                new ItemCarrinhoEntity(produtoId, nomeProduto, preco, quantidade)
        );

        return carrinhoRepository.save(carrinho);
    }

    @Override
    public CarrinhoEntity removerItem(Long carrinhoId, Long produtoId) {
        CarrinhoEntity carrinho = buscarPorId(carrinhoId);

        carrinho.removerItem(produtoId);

        return carrinhoRepository.save(carrinho);
    }

    @Override
    public BigDecimal calcularTotal(Long carrinhoId) {
        CarrinhoEntity carrinho = buscarPorId(carrinhoId);
        return carrinho.calcularTotal();
    }

    @Override
    public CarrinhoEntity buscarPorId(Long carrinhoId) {
        return carrinhoRepository.findById(carrinhoId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Carrinho não encontrado")
                );
    }
}
