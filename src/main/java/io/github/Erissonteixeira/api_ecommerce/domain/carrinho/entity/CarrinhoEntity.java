package io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity;

import io.github.Erissonteixeira.api_ecommerce.exception.NegocioException;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carrinhos")
public class CarrinhoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @OneToMany(mappedBy = "carrinho", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCarrinhoEntity> itens = new ArrayList<>();

    public CarrinhoEntity() {
    }

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = null;
    }

    @PreUpdate
    public void preUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }

    public void adicionarItem(ItemCarrinhoEntity novoItem) {
        ItemCarrinhoEntity itemExistente = itens.stream()
                .filter(item -> item.getProdutoId().equals(novoItem.getProdutoId()))
                .findFirst()
                .orElse(null);

        if (itemExistente != null) {
            itemExistente.incrementarQuantidade(novoItem.getQuantidade());
            return;
        }

        novoItem.setCarrinho(this);
        itens.add(novoItem);
    }

    public void removerItem(Long produtoId) {
        ItemCarrinhoEntity item = itens.stream()
                .filter(i -> i.getProdutoId().equals(produtoId))
                .findFirst()
                .orElseThrow(() -> new NegocioException("Produto não encontrado no carrinho"));

        if (item.getQuantidade() > 1) {
            item.decrementarQuantidade(1);
            return;
        }

        itens.remove(item);
    }

    public BigDecimal getTotal() {
        return itens.stream()
                .map(ItemCarrinhoEntity::calcularSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public List<ItemCarrinhoEntity> getItens() {
        return itens; }
}
