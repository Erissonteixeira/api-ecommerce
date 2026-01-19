package io.github.Erissonteixeira.api_ecommerce.domain.pedido.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class PedidoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPedido status;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoItemEntity> itens = new ArrayList<>();

    public PedidoEntity() {
    }

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
        this.status = StatusPedido.CRIADO;
    }

    public void adicionarItem(PedidoItemEntity item) {
        item.setPedido(this);
        this.itens.add(item);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<PedidoItemEntity> getItens() {
        return itens;
    }
}
