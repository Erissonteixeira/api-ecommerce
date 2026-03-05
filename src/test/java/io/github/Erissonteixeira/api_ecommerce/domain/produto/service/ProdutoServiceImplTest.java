package io.github.Erissonteixeira.api_ecommerce.domain.produto.service;

import io.github.Erissonteixeira.api_ecommerce.domain.produto.dto.ProdutoRequestDto;
import io.github.Erissonteixeira.api_ecommerce.domain.produto.dto.ProdutoResponseDto;
import io.github.Erissonteixeira.api_ecommerce.domain.produto.entity.ProdutoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.produto.mapper.ProdutoMapper;
import io.github.Erissonteixeira.api_ecommerce.domain.produto.repository.ProdutoRepository;
import io.github.Erissonteixeira.api_ecommerce.exception.RecursoNaoEncontradoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceImplTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ProdutoMapper produtoMapper;

    @InjectMocks
    private ProdutoServiceImpl produtoService;

    @Test
    void criar_deveSalvarEMapearResponse() {
        ProdutoRequestDto dto = new ProdutoRequestDto();
        dto.setNome("mouse gamer");
        dto.setPreco(new BigDecimal("129.90"));
        dto.setAtivo(true);

        ProdutoEntity entity = new ProdutoEntity();
        entity.setNome(dto.getNome());
        entity.setPreco(dto.getPreco());
        entity.setAtivo(dto.getAtivo());

        ProdutoEntity salvo = new ProdutoEntity();
        salvo.setId(1L);
        salvo.setNome(dto.getNome());
        salvo.setPreco(dto.getPreco());
        salvo.setAtivo(true);
        salvo.setCriadoEm(LocalDateTime.now());

        ProdutoResponseDto response = new ProdutoResponseDto();
        response.setId(1L);
        response.setNome(dto.getNome());
        response.setPreco(dto.getPreco());
        response.setAtivo(true);

        when(produtoMapper.toEntity(dto)).thenReturn(entity);
        when(produtoRepository.save(any(ProdutoEntity.class))).thenReturn(salvo);
        when(produtoMapper.toResponse(salvo)).thenReturn(response);

        ProdutoResponseDto resultado = produtoService.criar(dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("mouse gamer", resultado.getNome());
        assertEquals(new BigDecimal("129.90"), resultado.getPreco());
        assertTrue(resultado.getAtivo());

        verify(produtoRepository).save(any(ProdutoEntity.class));
        verify(produtoMapper).toEntity(dto);
        verify(produtoMapper).toResponse(salvo);
    }

    @Test
    void buscarPorId_quandoNaoExiste_deveLancar404() {
        when(produtoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNaoEncontradoException.class, () -> produtoService.buscarPorId(99L));
    }

    @Test
    void buscarPorId_quandoExiste_deveRetornarResponse() {
        ProdutoEntity entity = new ProdutoEntity();
        entity.setId(1L);
        entity.setNome("teclado");
        entity.setPreco(new BigDecimal("219.90"));
        entity.setAtivo(true);

        ProdutoResponseDto response = new ProdutoResponseDto();
        response.setId(1L);
        response.setNome("teclado");
        response.setPreco(new BigDecimal("219.90"));
        response.setAtivo(true);

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(produtoMapper.toResponse(entity)).thenReturn(response);

        ProdutoResponseDto resultado = produtoService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("teclado", resultado.getNome());
    }

    @Test
    void atualizar_quandoExiste_deveAtualizarCampos() {
        ProdutoRequestDto dto = new ProdutoRequestDto();
        dto.setNome("monitor gamer");
        dto.setPreco(new BigDecimal("899.90"));
        dto.setAtivo(true);

        ProdutoEntity existente = new ProdutoEntity();
        existente.setId(1L);
        existente.setNome("old");
        existente.setPreco(new BigDecimal("10.00"));
        existente.setAtivo(false);

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(produtoRepository.save(any(ProdutoEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        ProdutoResponseDto response = new ProdutoResponseDto();
        response.setId(1L);
        response.setNome(dto.getNome());
        response.setPreco(dto.getPreco());
        response.setAtivo(dto.getAtivo());

        when(produtoMapper.toResponse(any(ProdutoEntity.class))).thenReturn(response);

        ProdutoResponseDto atualizado = produtoService.atualizar(1L, dto);

        assertEquals("monitor gamer", atualizado.getNome());
        assertEquals(new BigDecimal("899.90"), atualizado.getPreco());
        assertTrue(atualizado.getAtivo());

        verify(produtoRepository).save(any(ProdutoEntity.class));
    }

    @Test
    void desativar_quandoExiste_deveMarcarAtivoFalse() {
        ProdutoEntity existente = new ProdutoEntity();
        existente.setId(1L);
        existente.setAtivo(true);

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(produtoRepository.save(any(ProdutoEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        produtoService.desativar(1L);

        assertFalse(existente.getAtivo());
        verify(produtoRepository).save(existente);
    }
}