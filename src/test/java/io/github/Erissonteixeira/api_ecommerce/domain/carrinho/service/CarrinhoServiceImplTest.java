package io.github.Erissonteixeira.api_ecommerce.domain.carrinho.service;

import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity.CarrinhoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity.ItemCarrinhoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.repository.CarrinhoRepository;
import io.github.Erissonteixeira.api_ecommerce.exception.NegocioException;
import io.github.Erissonteixeira.api_ecommerce.exception.RecursoNaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
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
class CarrinhoServiceImplTest {

    @Mock
    private CarrinhoRepository carrinhoRepository;

    @InjectMocks
    private CarrinhoServiceImpl service;

    private CarrinhoEntity carrinho;

    @BeforeEach
    void setup() {
        carrinho = new CarrinhoEntity();
    }

    @Test
    void criarCarrinho_deveSalvarECriarUmCarrinho() {
        when(carrinhoRepository.save(any(CarrinhoEntity.class)))
                .thenReturn(carrinho);

        CarrinhoEntity resultado = service.criarCarrinho();

        assertNotNull(resultado);
        verify(carrinhoRepository, times(1)).save(any(CarrinhoEntity.class));
    }

    @Test
    void adicionarItem_deveAdicionarItemExistente() {
        when(carrinhoRepository.buscarComItensPorId(1L))
                .thenReturn(Optional.of(carrinho));
        when(carrinhoRepository.save(any(CarrinhoEntity.class)))
                .thenReturn(carrinho);

        CarrinhoEntity resultado = service.adicionarItem(
                1L,
                10L,
                "Produto Teste",
                new BigDecimal("50.00"),
                2
        );

        assertEquals(1, resultado.getItens().size());

        ItemCarrinhoEntity item = resultado.getItens().get(0);
        assertEquals(10L, item.getProdutoId());
        assertEquals("Produto Teste", item.getNomeProduto());
        assertEquals(new BigDecimal("50.00"), item.getPrecoUnitario());
        assertEquals(2, item.getQuantidade());
    }

    @Test
    void adicionarItem_quantidadeZeroDeveLancarExcecao() {

        NegocioException exception = assertThrows(
                NegocioException.class,
                () -> service.adicionarItem(
                        1L,
                        10L,
                        "Produto Teste",
                        new BigDecimal("50.00"),
                        0
                )
        );

        assertEquals("Quantidade deve ser maior que zero", exception.getMessage());
    }

    @Test
    void removerItem_deveRemoverItem() {
        ItemCarrinhoEntity item =
                new ItemCarrinhoEntity(10L, "Produto Teste", new BigDecimal("50.00"), 1);

        carrinho.adicionarItem(item);

        when(carrinhoRepository.buscarComItensPorId(1L))
                .thenReturn(Optional.of(carrinho));
        when(carrinhoRepository.save(any(CarrinhoEntity.class)))
                .thenReturn(carrinho);

        CarrinhoEntity resultado = service.removerItem(1L, 10L);

        assertTrue(resultado.getItens().isEmpty());
    }

    @Test
    void removerItem_carrinhoNaoExistenteDeveLancarExcecao() {
        when(carrinhoRepository.buscarComItensPorId(1L))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.removerItem(1L, 10L)
        );

        assertEquals("Carrinho não encontrado", exception.getMessage());
    }

    @Test
    void calcularTotal_deveRetornarSomaItens() {
        carrinho.adicionarItem(
                new ItemCarrinhoEntity(10L, "Produto A", new BigDecimal("20.00"), 2)
        );
        carrinho.adicionarItem(
                new ItemCarrinhoEntity(11L, "Produto B", new BigDecimal("30.00"), 1)
        );

        when(carrinhoRepository.buscarComItensPorId(1L))
                .thenReturn(Optional.of(carrinho));

        BigDecimal total = service.calcularTotal(1L);

        assertEquals(new BigDecimal("70.00"), total);
    }

    @Test
    void adicionarItem_deveRemoverEspacosDoNomeProduto() {
        when(carrinhoRepository.buscarComItensPorId(1L))
                .thenReturn(Optional.of(carrinho));
        when(carrinhoRepository.save(any(CarrinhoEntity.class)))
                .thenReturn(carrinho);

        CarrinhoEntity resultado = service.adicionarItem(
                1L,
                10L,
                "  Produto Teste  ",
                new BigDecimal("50.00"),
                2
        );

        assertEquals("Produto Teste", resultado.getItens().get(0).getNomeProduto());
    }
}
