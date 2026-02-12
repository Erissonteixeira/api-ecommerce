package io.github.Erissonteixeira.api_ecommerce.domain.usuario.service;

import io.github.Erissonteixeira.api_ecommerce.domain.usuario.dto.UsuarioRequestDto;
import io.github.Erissonteixeira.api_ecommerce.domain.usuario.dto.UsuarioResponseDto;

import java.util.List;

public interface UsuarioService {
    UsuarioResponseDto criar(UsuarioRequestDto dto);
    UsuarioResponseDto buscarPorId(Long id);
    List<UsuarioResponseDto> listar();
    UsuarioResponseDto atualizar(Long id, UsuarioRequestDto dto);
    void remover(Long id);
}
