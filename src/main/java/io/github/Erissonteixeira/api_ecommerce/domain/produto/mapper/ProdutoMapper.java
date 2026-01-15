package io.github.Erissonteixeira.api_ecommerce.domain.produto.mapper;

import io.github.Erissonteixeira.api_ecommerce.domain.produto.dto.ProdutoRequestDto;
import io.github.Erissonteixeira.api_ecommerce.domain.produto.dto.ProdutoResponseDto;
import io.github.Erissonteixeira.api_ecommerce.domain.produto.entity.ProdutoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {
    ProdutoEntity toEntity(ProdutoRequestDto dto);
    ProdutoResponseDto toResponse(ProdutoEntity entity);
}