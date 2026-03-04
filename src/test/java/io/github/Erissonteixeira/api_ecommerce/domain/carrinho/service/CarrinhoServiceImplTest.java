package io.github.Erissonteixeira.api_ecommerce.domain.carrinho.service;

import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity.CarrinhoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity.ItemCarrinhoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.repository.CarrinhoRepository;
import io.github.Erissonteixeira.api_ecommerce.domain.produto.entity.ProdutoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.produto.repository.ProdutoRepository;
import io.github.Erissonteixeira.api_ecommerce.domain.usuario.entity.UsuarioEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.usuario.repository.UsuarioRepository;
import io.github.Erissonteixeira.api_ecommerce.exception.NegocioException;
import io.github.Erissonteixeira.api_ecommerce.exception.RecursoNaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
class CarrinhoServiceImplTest {

    @Mock
    private CarrinhoRepository carrinhoRepository;

    private static final String EMAIL = "user.teste01@email.com";
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private CarrinhoServiceImpl service;
    @Mock
    private ProdutoRepository produtoRepository;
    private UsuarioEntity usuario;
    private CarrinhoEntity carrinho;
    private ProdutoEntity produto;

    private static void setId(Object target, Long id) {
        setField(target, "id", id);
    }

    private static void setField(Object target, String fieldName, Object value) {
        try {
            Field f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(target, value);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Campo não encontrado: " + fieldName + " em " + target.getClass().getSimpleName(), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setup() {
        usuario = new UsuarioEntity();
        setId(usuario, 9L);

        carrinho = new CarrinhoEntity(usuario);

        produto = new ProdutoEntity();
        setId(produto, 10L);
        setField(produto, "nome", "Produto Teste");
        setField(produto, "preco", new BigDecimal("50.00"));
        setField(produto, "ativo", true);
    }

    @Test
    void obterOuCriarCarrinho_quandoExiste_deveRetornarCarrinho() {
        when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.of(usuario));
        when(carrinhoRepository.buscarPorUsuarioIdComItens(9L)).thenReturn(Optional.of(carrinho));

        CarrinhoEntity resultado = service.obterOuCriarCarrinho(EMAIL);

        assertNotNull(resultado);
        assertEquals(usuario, resultado.getUsuario());
        verify(carrinhoRepository, never()).save(any());
    }

    @Test
    void obterOuCriarCarrinho_quandoNaoExiste_deveCriarESalvar() {
        when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.of(usuario));
        when(carrinhoRepository.buscarPorUsuarioIdComItens(9L)).thenReturn(Optional.empty());
        when(carrinhoRepository.save(any(CarrinhoEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CarrinhoEntity resultado = service.obterOuCriarCarrinho(EMAIL);

        assertNotNull(resultado);
        assertEquals(usuario, resultado.getUsuario());
        verify(carrinhoRepository, times(1)).save(any(CarrinhoEntity.class));
    }

    @Test
    void adicionarItem_deveAdicionarItemNoCarrinho() {
        when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.of(usuario));
        when(carrinhoRepository.buscarPorUsuarioIdComItens(9L)).thenReturn(Optional.of(carrinho));
        when(produtoRepository.findById(10L)).thenReturn(Optional.of(produto));
        when(carrinhoRepository.save(any(CarrinhoEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CarrinhoEntity resultado = service.adicionarItem(EMAIL, 10L, 2);

        assertEquals(1, resultado.getItens().size());

        ItemCarrinhoEntity item = resultado.getItens().get(0);
        assertEquals(10L, item.getProdutoId());
        assertEquals("Produto Teste", item.getNomeProduto());
        assertEquals(new BigDecimal("50.00"), item.getPrecoUnitario());
        assertEquals(2, item.getQuantidade());
    }

    @Test
    void adicionarItem_quandoItemJaExiste_deveIncrementarQuantidade() {
        carrinho.adicionarItem(new ItemCarrinhoEntity(10L, "Produto Teste", new BigDecimal("50.00"), 1));

        when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.of(usuario));
        when(carrinhoRepository.buscarPorUsuarioIdComItens(9L)).thenReturn(Optional.of(carrinho));
        when(produtoRepository.findById(10L)).thenReturn(Optional.of(produto));
        when(carrinhoRepository.save(any(CarrinhoEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CarrinhoEntity resultado = service.adicionarItem(EMAIL, 10L, 2);

        assertEquals(1, resultado.getItens().size());
        assertEquals(3, resultado.getItens().get(0).getQuantidade());
    }

    @Test
    void adicionarItem_quantidadeZeroDeveLancarExcecao() {
        NegocioException exception = assertThrows(
                NegocioException.class,
                () -> service.adicionarItem(EMAIL, 10L, 0)
        );

        assertEquals("Quantidade deve ser maior que zero", exception.getMessage());
    }

    @Test
    void adicionarItem_produtoNaoEncontradoDeveLancarExcecao() {
        when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.of(usuario));
        when(carrinhoRepository.buscarPorUsuarioIdComItens(9L)).thenReturn(Optional.of(carrinho));
        when(produtoRepository.findById(10L)).thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.adicionarItem(EMAIL, 10L, 1)
        );

        assertEquals("Produto não encontrado", exception.getMessage());
    }

    @Test
    void adicionarItem_produtoInativoDeveLancarExcecao() {
        setField(produto, "ativo", false);

        when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.of(usuario));
        when(carrinhoRepository.buscarPorUsuarioIdComItens(9L)).thenReturn(Optional.of(carrinho));
        when(produtoRepository.findById(10L)).thenReturn(Optional.of(produto));

        NegocioException exception = assertThrows(
                NegocioException.class,
                () -> service.adicionarItem(EMAIL, 10L, 1)
        );

        assertEquals("Produto indisponível", exception.getMessage());
    }

    @Test
    void removerItem_deveRemoverItemQuandoQuantidadeUm() {
        carrinho.adicionarItem(new ItemCarrinhoEntity(10L, "Produto Teste", new BigDecimal("50.00"), 1));

        when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.of(usuario));
        when(carrinhoRepository.buscarPorUsuarioIdComItens(9L)).thenReturn(Optional.of(carrinho));
        when(carrinhoRepository.save(any(CarrinhoEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CarrinhoEntity resultado = service.removerItem(EMAIL, 10L);

        assertTrue(resultado.getItens().isEmpty());
    }

    @Test
    void removerItem_quandoCarrinhoNaoExiste_deveCriarEDevolverErroDeProdutoNoCarrinho() {
        when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.of(usuario));
        when(carrinhoRepository.buscarPorUsuarioIdComItens(9L)).thenReturn(Optional.empty());
        when(carrinhoRepository.save(any(CarrinhoEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        NegocioException exception = assertThrows(
                NegocioException.class,
                () -> service.removerItem(EMAIL, 10L)
        );

        assertEquals("Produto não encontrado no carrinho", exception.getMessage());
    }

    @Test
    void limpar_deveLimparCarrinho() {
        carrinho.adicionarItem(new ItemCarrinhoEntity(10L, "Produto Teste", new BigDecimal("50.00"), 2));

        when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.of(usuario));
        when(carrinhoRepository.buscarPorUsuarioIdComItens(9L)).thenReturn(Optional.of(carrinho));
        when(carrinhoRepository.save(any(CarrinhoEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        service.limpar(EMAIL);

        assertTrue(carrinho.getItens().isEmpty());
        verify(carrinhoRepository, times(1)).save(any(CarrinhoEntity.class));
    }
}