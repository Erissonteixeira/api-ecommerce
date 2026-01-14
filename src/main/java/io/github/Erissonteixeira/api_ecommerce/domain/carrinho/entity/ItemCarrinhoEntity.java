package io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity;

import java.math.BigDecimal;
import java.util.Objects;

public class ItemCarrinhoEntity {

    private Long produtoId;
    private String nomeProduto;
    private BigDecimal precoUnitario;
    private Integer quantidade;

    public ItemCarrinhoEntity(
            Long produtoId,
            String nomeProduto,
            BigDecimal precoUnitario,
            Integer quantidade
    ) {
        this.produtoId = produtoId;
        this.nomeProduto = nomeProduto;
        this.precoUnitario = precoUnitario;
        this.quantidade = quantidade;
    }

    public BigDecimal calcularSubtotal() {
        return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

    public void incrementarQuantidade(Integer quantidade) {
        this.quantidade += quantidade;
    }

    public void decrementarQuantidade(Integer quantidade) {
        this.quantidade -= quantidade;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemCarrinhoEntity that)) return false;
        return Objects.equals(produtoId, that.produtoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(produtoId);
    }
}
