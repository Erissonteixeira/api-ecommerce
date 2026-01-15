package io.github.Erissonteixeira.api_ecommerce.domain.produto.service;

import io.github.Erissonteixeira.api_ecommerce.domain.produto.dto.ProdutoRequestDto;
import io.github.Erissonteixeira.api_ecommerce.domain.produto.dto.ProdutoResponseDto;
import io.github.Erissonteixeira.api_ecommerce.domain.produto.entity.ProdutoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.produto.entity.ProdutoRepository;
import io.github.Erissonteixeira.api_ecommerce.domain.produto.mapper.ProdutoMapper;
import io.github.Erissonteixeira.api_ecommerce.exception.RecursoNaoEncontradoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceImplTest {

    @Mock
    ProdutoRepository produtoRepository;

    @Mock
    ProdutoMapper produtoMapper;

    @InjectMocks
    ProdutoServiceImpl produtoService;

    private ProdutoRequestDto dtoValido() {
        var dto = new ProdutoRequestDto();
        dto.setNome("Produto Teste");
        dto.setPreco(new BigDecimal("10.50"));
        dto.setAtivo(true);
        return dto;
    }

    private ProdutoEntity entityBase(Long id, boolean ativo) {
        var e = new ProdutoEntity();
        e.setId(id);
        e.setNome("Produto Teste");
        e.setPreco(new BigDecimal("10.50"));
        e.setAtivo(ativo);
        e.setCriadoEm(LocalDateTime.now().minusDays(1));
        e.setAtualizadoEm(null);
        return e;
    }

    private ProdutoResponseDto response(Long id, boolean ativo) {
        var r = new ProdutoResponseDto();
        r.setId(id);
        r.setNome("Produto Teste");
        r.setPreco(new BigDecimal("10.50"));
        r.setAtivo(ativo);
        r.setCriadoEm(LocalDateTime.now().minusDays(1));
        r.setAtualizadoEm(null);
        return r;
    }

    @Test
    void criar_deveSetarCriadoEm_eAtualizadoEmNull_eSalvar() {
        var dto = dtoValido();
        var entitySemDatas = new ProdutoEntity();
        entitySemDatas.setNome(dto.getNome());
        entitySemDatas.setPreco(dto.getPreco());
        entitySemDatas.setAtivo(dto.getAtivo());

        var salvo = entityBase(1L, true);
        salvo.setAtualizadoEm(null); // como teu service deixa null no create

        var resp = response(1L, true);

        when(produtoMapper.toEntity(dto)).thenReturn(entitySemDatas);
        when(produtoRepository.save(any(ProdutoEntity.class))).thenReturn(salvo);
        when(produtoMapper.toResponse(salvo)).thenReturn(resp);

        var resultado = produtoService.criar(dto);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);

        ArgumentCaptor<ProdutoEntity> captor = ArgumentCaptor.forClass(ProdutoEntity.class);
        verify(produtoRepository).save(captor.capture());

        ProdutoEntity enviadoParaSalvar = captor.getValue();
        assertThat(enviadoParaSalvar.getCriadoEm()).isNotNull();
        assertThat(enviadoParaSalvar.getAtualizadoEm()).isNull();

        verify(produtoMapper).toEntity(dto);
        verify(produtoMapper).toResponse(salvo);
    }

    @Test
    void buscarPorId_quandoExiste_deveRetornarResponse() {
        var entity = entityBase(10L, true);
        var resp = response(10L, true);

        when(produtoRepository.findById(10L)).thenReturn(Optional.of(entity));
        when(produtoMapper.toResponse(entity)).thenReturn(resp);

        var resultado = produtoService.buscarPorId(10L);

        assertThat(resultado.getId()).isEqualTo(10L);
        verify(produtoRepository).findById(10L);
        verify(produtoMapper).toResponse(entity);
    }

    @Test
    void buscarPorId_quandoNaoExiste_deveLancarRecursoNaoEncontrado() {
        when(produtoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> produtoService.buscarPorId(99L))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Produto não encontrado");

        verify(produtoRepository).findById(99L);
        verifyNoInteractions(produtoMapper);
    }

    @Test
    void listarTodos_deveMapearCadaEntityParaResponse() {
        var e1 = entityBase(1L, true);
        var e2 = entityBase(2L, false);

        var r1 = response(1L, true);
        var r2 = response(2L, false);

        when(produtoRepository.findAll()).thenReturn(List.of(e1, e2));
        when(produtoMapper.toResponse(e1)).thenReturn(r1);
        when(produtoMapper.toResponse(e2)).thenReturn(r2);

        var lista = produtoService.listarTodos();

        assertThat(lista).hasSize(2);
        assertThat(lista.get(0).getId()).isEqualTo(1L);
        assertThat(lista.get(1).getId()).isEqualTo(2L);

        verify(produtoRepository).findAll();
        verify(produtoMapper).toResponse(e1);
        verify(produtoMapper).toResponse(e2);
    }

    @Test
    void listarAtivos_deveChamarFindByAtivoTrue_eMapear() {
        var e1 = entityBase(1L, true);
        var r1 = response(1L, true);

        when(produtoRepository.findByAtivoTrue()).thenReturn(List.of(e1));
        when(produtoMapper.toResponse(e1)).thenReturn(r1);

        var lista = produtoService.listarAtivos();

        assertThat(lista).hasSize(1);
        assertThat(lista.get(0).getAtivo()).isTrue();

        verify(produtoRepository).findByAtivoTrue();
        verify(produtoMapper).toResponse(e1);
    }

    @Test
    void atualizar_quandoExiste_deveAtualizarCampos_setarAtualizadoEm_eSalvar() {
        var dto = dtoValido();
        dto.setNome("Nome Novo");
        dto.setPreco(new BigDecimal("99.99"));
        dto.setAtivo(false);

        var existente = entityBase(5L, true);
        existente.setAtualizadoEm(null);

        var resp = new ProdutoResponseDto();
        resp.setId(5L);
        resp.setNome("Nome Novo");
        resp.setPreco(new BigDecimal("99.99"));
        resp.setAtivo(false);

        when(produtoRepository.findById(5L)).thenReturn(Optional.of(existente));
        when(produtoRepository.save(any(ProdutoEntity.class))).thenAnswer(inv -> inv.getArgument(0));
        when(produtoMapper.toResponse(any(ProdutoEntity.class))).thenReturn(resp);

        var resultado = produtoService.atualizar(5L, dto);

        assertThat(resultado.getId()).isEqualTo(5L);
        assertThat(existente.getNome()).isEqualTo("Nome Novo");
        assertThat(existente.getPreco()).isEqualByComparingTo("99.99");
        assertThat(existente.getAtivo()).isFalse();
        assertThat(existente.getAtualizadoEm()).isNotNull();

        verify(produtoRepository).findById(5L);
        verify(produtoRepository).save(existente);
        verify(produtoMapper).toResponse(existente);
    }
}
