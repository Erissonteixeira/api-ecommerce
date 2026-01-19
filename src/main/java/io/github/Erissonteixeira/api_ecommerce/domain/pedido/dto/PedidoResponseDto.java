package io.github.Erissonteixeira.api_ecommerce.domain.pedido.dto;

import io.github.Erissonteixeira.api_ecommerce.domain.pedido.entity.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PedidoResponseDto {

    private Long id;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
    private StatusPedido status;
    private BigDecimal total;
    private List<PedidoItemResponseDto> itens;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<PedidoItemResponseDto> getItens() {
        return itens;
    }

    public void setItens(List<PedidoItemResponseDto> itens) {
        this.itens = itens;
    }
}
