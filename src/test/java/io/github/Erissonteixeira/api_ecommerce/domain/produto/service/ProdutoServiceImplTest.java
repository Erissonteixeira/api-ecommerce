package io.github.Erissonteixeira.api_ecommerce.domain.produto.service;

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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceImplTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ProdutoMapper produtoMapper;

    @InjectMocks
    private ProdutoServiceImpl produtoService;

    @Test
    void buscarPorId_quandoNaoExiste_deveLancar404() {
        Long id = 999L;

        when(produtoRepository.findByIdAndAtivoTrue(id)).thenReturn(Optional.empty());

        RecursoNaoEncontradoException ex = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> produtoService.buscarPorId(id)
        );

        assertEquals("Produto não encontrado", ex.getMessage());

        verify(produtoRepository).findByIdAndAtivoTrue(id);
        verifyNoInteractions(produtoMapper);
    }

    @Test
    void buscarPorId_quandoExiste_deveRetornarResponse() {
        Long id = 1L;

        ProdutoEntity entity = new ProdutoEntity();
        entity.setId(id);
        entity.setNome("mouse gamer");
        entity.setPreco(new BigDecimal("150.00"));
        entity.setAtivo(true);

        ProdutoResponseDto dto = new ProdutoResponseDto();
        dto.setId(id);
        dto.setNome("mouse gamer");
        dto.setPreco(new BigDecimal("150.00"));
        dto.setAtivo(true);

        when(produtoRepository.findByIdAndAtivoTrue(id)).thenReturn(Optional.of(entity));
        when(produtoMapper.toResponse(entity)).thenReturn(dto);

        ProdutoResponseDto resp = produtoService.buscarPorId(id);

        assertNotNull(resp);
        assertEquals(id, resp.getId());
        assertEquals("mouse gamer", resp.getNome());
        assertEquals(new BigDecimal("150.00"), resp.getPreco());
        assertTrue(resp.getAtivo());

        verify(produtoRepository).findByIdAndAtivoTrue(id);
        verify(produtoMapper).toResponse(entity);
    }
}