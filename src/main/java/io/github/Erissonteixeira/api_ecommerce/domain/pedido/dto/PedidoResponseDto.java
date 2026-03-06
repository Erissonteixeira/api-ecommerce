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

    private Long usuarioId;
    private String usuarioNome;
    private String usuarioEmail;

    private List<PedidoItemResponseDto> itens;

    public PedidoResponseDto() {
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

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioNome() {
        return usuarioNome;
    }

    public List<PedidoItemResponseDto> getItens() {
        return itens;
    }

    public void setUsuarioNome(String usuarioNome) {
        this.usuarioNome = usuarioNome;
    }

    public String getUsuarioEmail() {
        return usuarioEmail;
    }

    public void setUsuarioEmail(String usuarioEmail) {
        this.usuarioEmail = usuarioEmail;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void setItens(List<PedidoItemResponseDto> itens) {
        this.itens = itens;
    }
}