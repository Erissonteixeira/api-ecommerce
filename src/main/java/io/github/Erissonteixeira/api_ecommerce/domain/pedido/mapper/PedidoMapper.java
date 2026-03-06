package io.github.Erissonteixeira.api_ecommerce.domain.pedido.mapper;

import io.github.Erissonteixeira.api_ecommerce.domain.pedido.dto.PedidoItemResponseDto;
import io.github.Erissonteixeira.api_ecommerce.domain.pedido.dto.PedidoResponseDto;
import io.github.Erissonteixeira.api_ecommerce.domain.pedido.entity.PedidoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.pedido.entity.PedidoItemEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PedidoMapper {

    public PedidoResponseDto toResponseDto(PedidoEntity entity) {
        PedidoResponseDto dto = new PedidoResponseDto();
        dto.setId(entity.getId());
        dto.setCriadoEm(entity.getCriadoEm());
        dto.setAtualizadoEm(entity.getAtualizadoEm());
        dto.setStatus(entity.getStatus());
        dto.setTotal(entity.getTotal());

        if (entity.getUsuario() != null) {
            dto.setUsuarioId(entity.getUsuario().getId());
            dto.setUsuarioNome(entity.getUsuario().getNome());
            dto.setUsuarioEmail(entity.getUsuario().getEmail());
        }

        dto.setItens(entity.getItens().stream()
                .map(this::toItemResponseDto)
                .toList());

        return dto;
    }

    public List<PedidoResponseDto> toResponseDtoList(List<PedidoEntity> entities) {
        return entities.stream()
                .map(this::toResponseDto)
                .toList();
    }

    private PedidoItemResponseDto toItemResponseDto(PedidoItemEntity entity) {
        PedidoItemResponseDto dto = new PedidoItemResponseDto();
        dto.setId(entity.getId());
        dto.setProdutoId(entity.getProdutoId());
        dto.setNomeProduto(entity.getNomeProduto());
        dto.setPrecoUnitario(entity.getPrecoUnitario());
        dto.setQuantidade(entity.getQuantidade());
        dto.setSubtotal(entity.getSubtotal());
        return dto;
    }
}