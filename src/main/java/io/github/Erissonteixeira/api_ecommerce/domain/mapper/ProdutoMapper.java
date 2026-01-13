package io.github.Erissonteixeira.api_ecommerce.domain.mapper;

import io.github.Erissonteixeira.api_ecommerce.domain.dto.ProdutoRequestDto;
import io.github.Erissonteixeira.api_ecommerce.domain.dto.ProdutoResponseDto;
import io.github.Erissonteixeira.api_ecommerce.domain.entity.ProdutoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {

    ProdutoMapper INSTANCE = Mappers.getMapper(ProdutoMapper.class);

    ProdutoEntity toEntity(ProdutoRequestDto dto);

    ProdutoResponseDto toResponse(ProdutoEntity entity);
}
