package io.github.Erissonteixeira.api_ecommerce.domain.carrinho.service;

import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity.CarrinhoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity.ItemCarrinhoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.repository.CarrinhoRepository;
import io.github.Erissonteixeira.api_ecommerce.exception.NegocioException;
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
        when(carrinhoRepository.findById(1L))
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
        when(carrinhoRepository.findById(1L))
                .thenReturn(Optional.of(carrinho));

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
}
