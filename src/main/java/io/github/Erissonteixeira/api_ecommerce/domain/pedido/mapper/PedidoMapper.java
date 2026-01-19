package io.github.Erissonteixeira.api_ecommerce.domain.pedido.mapper;

import io.github.Erissonteixeira.api_ecommerce.domain.pedido.dto.PedidoItemResponseDto;
import io.github.Erissonteixeira.api_ecommerce.domain.pedido.dto.PedidoResponseDto;
import io.github.Erissonteixeira.api_ecommerce.domain.pedido.entity.PedidoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.pedido.entity.PedidoItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PedidoMapper {

    PedidoResponseDto toResponseDto(PedidoEntity entity);

    default PedidoResponseDto toResponse(PedidoEntity entity) {
        return toResponseDto(entity);
    }

    @Mapping(target = "subtotal", expression = "java(entity.getSubtotal())")
    PedidoItemResponseDto toItemResponseDto(PedidoItemEntity entity);
}
