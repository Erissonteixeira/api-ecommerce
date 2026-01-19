package io.github.Erissonteixeira.api_ecommerce.domain.pedido.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class PedidoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<PedidoItemEntity> itens = new ArrayList<>();
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusPedido status;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    public PedidoEntity() {
    }

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
        if (this.status == null) {
            this.status = StatusPedido.CRIADO;
        }
        if (this.total == null) {
            this.total = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.atualizadoEm = LocalDateTime.now();
        if (this.total == null) {
            this.total = BigDecimal.ZERO;
        }
    }

    public void adicionarItem(PedidoItemEntity item) {
        if (item == null) return;
        item.setPedido(this);
        this.itens.add(item);
    }

    public void removerItem(PedidoItemEntity item) {
        if (item == null) return;
        this.itens.remove(item);
        item.setPedido(null);
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

    public StatusPedido getStatus() {
        return status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total == null ? BigDecimal.ZERO : total;
    }

    public List<PedidoItemEntity> getItens() {
        return Collections.unmodifiableList(itens);
    }
}
