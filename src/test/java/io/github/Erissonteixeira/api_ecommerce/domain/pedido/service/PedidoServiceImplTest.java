package io.github.Erissonteixeira.api_ecommerce.domain.pedido.service;

import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity.CarrinhoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity.ItemCarrinhoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.repository.CarrinhoRepository;
import io.github.Erissonteixeira.api_ecommerce.domain.pedido.entity.PedidoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.pedido.repository.PedidoRepository;
import io.github.Erissonteixeira.api_ecommerce.exception.RecursoNaoEncontradoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceImplTest {

    @Mock
    private CarrinhoRepository carrinhoRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoServiceImpl pedidoService;

    @Captor
    private ArgumentCaptor<PedidoEntity> pedidoCaptor;

    @Test
    void criarPedidoDoCarrinho_quandoCarrinhoNaoExiste_deveLancar404() {
        Long carrinhoId = 99L;
        when(carrinhoRepository.buscarComItensPorId(carrinhoId)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> pedidoService.criarPedidoDoCarrinho(carrinhoId));

        verify(carrinhoRepository).buscarComItensPorId(carrinhoId);
        verifyNoInteractions(pedidoRepository);
    }

    @Test
    void criarPedidoDoCarrinho_comItens_deveGerarPedidoComSnapshotESalvar() {
        Long carrinhoId = 1L;
        CarrinhoEntity carrinho = carrinhoComItens();

        when(carrinhoRepository.buscarComItensPorId(carrinhoId)).thenReturn(Optional.of(carrinho));
        when(pedidoRepository.save(any(PedidoEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0, PedidoEntity.class));

        PedidoEntity pedido = pedidoService.criarPedidoDoCarrinho(carrinhoId);

        assertNotNull(pedido);
        assertEquals(2, pedido.getItens().size());
        assertEquals(new BigDecimal("600.00"), pedido.getTotal());

        verify(carrinhoRepository).buscarComItensPorId(carrinhoId);
        verify(pedidoRepository).save(pedidoCaptor.capture());

        PedidoEntity salvo = pedidoCaptor.getValue();
        assertEquals(new BigDecimal("600.00"), salvo.getTotal());
        assertEquals(2, salvo.getItens().size());
    }

    @Test
    void criarPedidoDoCarrinho_quandoCarrinhoVazio_deveCriarPedidoComTotalZero() {
        Long carrinhoId = 1L;
        CarrinhoEntity carrinhoVazio = new CarrinhoEntity(); // itens = []

        when(carrinhoRepository.buscarComItensPorId(carrinhoId)).thenReturn(Optional.of(carrinhoVazio));
        when(pedidoRepository.save(any(PedidoEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0, PedidoEntity.class));

        PedidoEntity pedido = pedidoService.criarPedidoDoCarrinho(carrinhoId);

        assertNotNull(pedido);
        assertTrue(pedido.getItens().isEmpty());
        assertEquals(BigDecimal.ZERO, pedido.getTotal());

        verify(carrinhoRepository).buscarComItensPorId(carrinhoId);
        verify(pedidoRepository).save(any(PedidoEntity.class));
    }

    private CarrinhoEntity carrinhoComItens() {
        CarrinhoEntity c = new CarrinhoEntity();

        c.adicionarItem(new ItemCarrinhoEntity(
                10L, "mouse gamer", new BigDecimal("150.00"), 2
        )); // 300

        c.adicionarItem(new ItemCarrinhoEntity(
                11L, "teclado mecanico", new BigDecimal("150.00"), 2
        )); // 300

        return c;
    }
}
