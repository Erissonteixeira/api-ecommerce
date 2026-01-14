package io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity;

import io.github.Erissonteixeira.api_ecommerce.exception.NegocioException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CarrinhoEntity {

    private final List<ItemCarrinhoEntity> itens = new ArrayList<>();

    public void adicionarItem(ItemCarrinhoEntity novoItem) {
        ItemCarrinhoEntity itemExistente = itens.stream()
                .filter(item -> item.getProdutoId().equals(novoItem.getProdutoId()))
                .findFirst()
                .orElse(null);

        if (itemExistente != null) {
            itemExistente.incrementarQuantidade(novoItem.getQuantidade());
            return;
        }

        itens.add(novoItem);
    }

    public void removerItem(Long produtoId) {
        ItemCarrinhoEntity item = itens.stream()
                .filter(i -> i.getProdutoId().equals(produtoId))
                .findFirst()
                .orElseThrow(() ->
                        new NegocioException("Produto não encontrado no carrinho")
                );

        if (item.getQuantidade() > 1) {
            item.decrementarQuantidade(1);
            return;
        }

        itens.remove(item);
    }

    public BigDecimal calcularTotal() {
        return itens.stream()
                .map(ItemCarrinhoEntity::calcularSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<ItemCarrinhoEntity> getItens() {
        return itens;
    }
}
