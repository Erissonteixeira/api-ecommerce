package io.github.Erissonteixeira.api_ecommerce.domain.pedido.service;

import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity.CarrinhoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity.ItemCarrinhoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.repository.CarrinhoRepository;
import io.github.Erissonteixeira.api_ecommerce.domain.pedido.entity.PedidoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.pedido.repository.PedidoRepository;
import io.github.Erissonteixeira.api_ecommerce.domain.usuario.entity.UsuarioEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.usuario.repository.UsuarioRepository;
import io.github.Erissonteixeira.api_ecommerce.exception.NegocioException;
import io.github.Erissonteixeira.api_ecommerce.exception.RecursoNaoEncontradoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceImplTest {

    private static final String EMAIL = "user.teste01@email.com";

    @Mock
    private CarrinhoRepository carrinhoRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private PedidoServiceImpl pedidoService;

    @Captor
    private ArgumentCaptor<PedidoEntity> pedidoCaptor;

    private static void setField(Object target, String fieldName, Object value) {
        try {
            Field f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException("Falha ao setar campo '" + fieldName + "' em " + target.getClass().getSimpleName(), e);
        }
    }

    @Test
    void criarPedidoDoCarrinho_quandoUsuarioNaoExiste_deveLancar404() {
        when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> pedidoService.criarPedidoDoCarrinho(EMAIL));

        verify(usuarioRepository).findByEmail(EMAIL);
        verifyNoInteractions(carrinhoRepository);
        verifyNoInteractions(pedidoRepository);
    }

    @Test
    void criarPedidoDoCarrinho_quandoCarrinhoNaoExiste_deveLancar404() {
        UsuarioEntity usuario = usuarioComId(9L);

        when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.of(usuario));
        when(carrinhoRepository.buscarPorUsuarioIdComItens(9L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> pedidoService.criarPedidoDoCarrinho(EMAIL));

        verify(usuarioRepository).findByEmail(EMAIL);
        verify(carrinhoRepository).buscarPorUsuarioIdComItens(9L);
        verifyNoInteractions(pedidoRepository);
    }

    @Test
    void criarPedidoDoCarrinho_quandoCarrinhoVazio_deveLancarNegocioException() {
        UsuarioEntity usuario = usuarioComId(9L);
        CarrinhoEntity carrinhoVazio = new CarrinhoEntity(usuario);

        when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.of(usuario));
        when(carrinhoRepository.buscarPorUsuarioIdComItens(9L)).thenReturn(Optional.of(carrinhoVazio));

        NegocioException ex = assertThrows(NegocioException.class,
                () -> pedidoService.criarPedidoDoCarrinho(EMAIL));

        assertEquals("Não é possível gerar pedido com carrinho vazio", ex.getMessage());

        verify(usuarioRepository).findByEmail(EMAIL);
        verify(carrinhoRepository).buscarPorUsuarioIdComItens(9L);
        verifyNoInteractions(pedidoRepository);
    }


    @Test
    void criarPedidoDoCarrinho_comItens_deveGerarPedidoESalvarELimparCarrinho() {
        UsuarioEntity usuario = usuarioComId(9L);
        CarrinhoEntity carrinho = carrinhoComItens(usuario);

        when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.of(usuario));
        when(carrinhoRepository.buscarPorUsuarioIdComItens(9L)).thenReturn(Optional.of(carrinho));

        when(pedidoRepository.save(any(PedidoEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0, PedidoEntity.class));

        when(carrinhoRepository.save(any(CarrinhoEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0, CarrinhoEntity.class));

        PedidoEntity pedido = pedidoService.criarPedidoDoCarrinho(EMAIL);

        assertNotNull(pedido);
        assertEquals(2, pedido.getItens().size());

        assertTrue(carrinho.getItens().isEmpty());

        verify(usuarioRepository).findByEmail(EMAIL);
        verify(carrinhoRepository).buscarPorUsuarioIdComItens(9L);

        verify(pedidoRepository).save(pedidoCaptor.capture());
        PedidoEntity salvo = pedidoCaptor.getValue();
        assertNotNull(salvo);
        assertEquals(2, salvo.getItens().size());

        verify(carrinhoRepository).save(any(CarrinhoEntity.class));
    }

    private UsuarioEntity usuarioComId(Long id) {
        UsuarioEntity u = new UsuarioEntity();
        setField(u, "id", id);
        return u;
    }

    private CarrinhoEntity carrinhoComItens(UsuarioEntity usuario) {
        CarrinhoEntity c = new CarrinhoEntity(usuario);

        c.adicionarItem(new ItemCarrinhoEntity(
                10L, "mouse gamer", new BigDecimal("150.00"), 2
        ));

        c.adicionarItem(new ItemCarrinhoEntity(
                11L, "teclado mecanico", new BigDecimal("150.00"), 2
        ));

        return c;
    }
}