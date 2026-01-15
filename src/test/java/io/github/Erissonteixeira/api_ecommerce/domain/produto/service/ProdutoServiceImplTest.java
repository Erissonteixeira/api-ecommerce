package io.github.Erissonteixeira.api_ecommerce.domain.produto.service;

import io.github.Erissonteixeira.api_ecommerce.domain.produto.dto.ProdutoRequestDto;
import io.github.Erissonteixeira.api_ecommerce.domain.produto.dto.ProdutoResponseDto;
import io.github.Erissonteixeira.api_ecommerce.domain.produto.entity.ProdutoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.produto.entity.ProdutoRepository;
import io.github.Erissonteixeira.api_ecommerce.domain.produto.mapper.ProdutoMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
}
