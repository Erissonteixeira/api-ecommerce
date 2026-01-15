package io.github.Erissonteixeira.api_ecommerce.domain.carrinho.mapper;

import io.github.Erissonteixeira.api_ecommerce.domain.dto.CarrinhoItemResponseDto;
import io.github.Erissonteixeira.api_ecommerce.domain.dto.CarrinhoResponseDto;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity.CarrinhoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity.ItemCarrinhoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CarrinhoMapper {

    @Mapping(target = "total", expression = "java(entity.calcularTotal())")
    CarrinhoResponseDto toResponse(CarrinhoEntity entity);

    @Mapping(target = "subtotal", expression = "java(item.calcularSubtotal())")
    CarrinhoItemResponseDto toItemResponse(ItemCarrinhoEntity item);
}
