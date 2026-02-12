package io.github.Erissonteixeira.api_ecommerce.domain.usuario.mapper;

import io.github.Erissonteixeira.api_ecommerce.domain.usuario.dto.UsuarioRequestDto;
import io.github.Erissonteixeira.api_ecommerce.domain.usuario.dto.UsuarioResponseDto;
import io.github.Erissonteixeira.api_ecommerce.domain.usuario.entity.UsuarioEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    UsuarioEntity toEntity(UsuarioRequestDto dto);
    UsuarioResponseDto toResponse(UsuarioEntity entity);
}
