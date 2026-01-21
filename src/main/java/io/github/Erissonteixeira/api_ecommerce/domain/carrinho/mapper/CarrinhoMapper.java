package io.github.Erissonteixeira.api_ecommerce.domain.carrinho.mapper;

import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.dto.CarrinhoItemResponseDto;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.dto.CarrinhoResponseDto;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity.CarrinhoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity.ItemCarrinhoEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CarrinhoMapper {

    CarrinhoResponseDto toResponseDto(CarrinhoEntity entity);

    CarrinhoItemResponseDto toItemResponseDto(ItemCarrinhoEntity entity);

    List<CarrinhoItemResponseDto> toItemResponseDtoList(List<ItemCarrinhoEntity> itens);
}
