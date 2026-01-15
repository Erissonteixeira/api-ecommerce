package io.github.Erissonteixeira.api_ecommerce.domain.produto.service;

import io.github.Erissonteixeira.api_ecommerce.domain.produto.dto.ProdutoRequestDto;
import io.github.Erissonteixeira.api_ecommerce.domain.produto.dto.ProdutoResponseDto;

import java.util.List;

public interface ProdutoService {

    ProdutoResponseDto criar(ProdutoRequestDto dto);

    ProdutoResponseDto buscarPorId(Long id);

    List<ProdutoResponseDto> listarTodos();

    List<ProdutoResponseDto> listarAtivos();

    ProdutoResponseDto atualizar(Long id, ProdutoRequestDto dto);

    void desativar(Long id);
}
