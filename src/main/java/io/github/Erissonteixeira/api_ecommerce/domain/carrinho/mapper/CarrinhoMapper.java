package io.github.Erissonteixeira.api_ecommerce.domain.carrinho.mapper;

import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.dto.CarrinhoItemResponseDto;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.dto.CarrinhoResponseDto;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity.CarrinhoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity.ItemCarrinhoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CarrinhoMapper {

    @Mapping(target = "total", expression = "java(entity.getTotal())")
    CarrinhoResponseDto toResponseDto(CarrinhoEntity entity);

    @Mapping(target = "subtotal", expression = "java(entity.getSubtotal())")
    CarrinhoItemResponseDto toItemResponseDto(ItemCarrinhoEntity entity);
}