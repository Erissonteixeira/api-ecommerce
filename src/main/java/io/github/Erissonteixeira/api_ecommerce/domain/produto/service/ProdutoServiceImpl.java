package io.github.Erissonteixeira.api_ecommerce.domain.produto.service;

import io.github.Erissonteixeira.api_ecommerce.domain.produto.dto.ProdutoRequestDto;
import io.github.Erissonteixeira.api_ecommerce.domain.produto.dto.ProdutoResponseDto;
import io.github.Erissonteixeira.api_ecommerce.domain.produto.entity.ProdutoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.produto.entity.ProdutoRepository;
import io.github.Erissonteixeira.api_ecommerce.domain.produto.mapper.ProdutoMapper;
import io.github.Erissonteixeira.api_ecommerce.exception.RecursoNaoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper;

    public ProdutoServiceImpl(ProdutoRepository produtoRepository, ProdutoMapper produtoMapper) {
        this.produtoRepository = produtoRepository;
        this.produtoMapper = produtoMapper;
    }

    @Override
    public ProdutoResponseDto criar(ProdutoRequestDto dto) {
        ProdutoEntity entity = produtoMapper.toEntity(dto);
        entity.setCriadoEm(LocalDateTime.now());
        entity.setAtualizadoEm(null);

        ProdutoEntity salvo = produtoRepository.save(entity);
        return produtoMapper.toResponse(salvo);
    }

    @Override
    @Transactional(readOnly = true)
    public ProdutoResponseDto buscarPorId(Long id) {
        ProdutoEntity entity = produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado"));
        return produtoMapper.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResponseDto> listarTodos() {
        return produtoRepository.findAll()
                .stream()
                .map(produtoMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResponseDto> listarAtivos() {
        return produtoRepository.findByAtivoTrue()
                .stream()
                .map(produtoMapper::toResponse)
                .toList();
    }

    @Override
    public ProdutoResponseDto atualizar(Long id, ProdutoRequestDto dto) {
        ProdutoEntity existente = produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado"));

        existente.setNome(dto.getNome());
        existente.setPreco(dto.getPreco());
        existente.setAtivo(dto.getAtivo());
        existente.setAtualizadoEm(LocalDateTime.now());

        ProdutoEntity atualizado = produtoRepository.save(existente);
        return produtoMapper.toResponse(atualizado);
    }

    @Override
    public void desativar(Long id) {
        ProdutoEntity existente = produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado"));

        existente.setAtivo(false);
        existente.setAtualizadoEm(LocalDateTime.now());
        produtoRepository.save(existente);
    }
}
