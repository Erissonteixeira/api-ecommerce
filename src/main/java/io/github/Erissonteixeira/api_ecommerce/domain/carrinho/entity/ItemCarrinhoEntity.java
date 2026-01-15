package io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "itens_carrinho")
public class ItemCarrinhoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrinho_id", nullable = false)
    private CarrinhoEntity carrinho;

    @Column(name = "produto_id", nullable = false)
    private Long produtoId;

    @Column(name = "nome_produto", nullable = false, length = 100)
    private String nomeProduto;

    @Column(name = "preco_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnitario;

    @Column(nullable = false)
    private Integer quantidade;

    public ItemCarrinhoEntity() {
    }

    public ItemCarrinhoEntity(Long produtoId, String nomeProduto, BigDecimal precoUnitario, Integer quantidade) {
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

    public Long getId() {
        return id;
    }

    public CarrinhoEntity getCarrinho() {
        return carrinho;
    }

    public void setCarrinho(CarrinhoEntity carrinho) {
        this.carrinho = carrinho;
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
}
