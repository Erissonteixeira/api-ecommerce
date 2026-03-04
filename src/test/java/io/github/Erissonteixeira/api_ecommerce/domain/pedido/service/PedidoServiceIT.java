package io.github.Erissonteixeira.api_ecommerce.domain.pedido.service;

import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity.CarrinhoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity.ItemCarrinhoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.repository.CarrinhoRepository;
import io.github.Erissonteixeira.api_ecommerce.domain.pedido.entity.PedidoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.pedido.repository.PedidoRepository;
import io.github.Erissonteixeira.api_ecommerce.domain.usuario.entity.UsuarioEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class PedidoServiceIT {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private CarrinhoRepository carrinhoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private static void setField(Object target, String fieldName, Object value) {
        try {
            Field f = findField(target.getClass(), fieldName);
            f.setAccessible(true);
            f.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException("Falha ao setar campo '" + fieldName + "' em " + target.getClass().getSimpleName(), e);
        }
    }

    private static Field findField(Class<?> type, String fieldName) throws NoSuchFieldException {
        Class<?> current = type;
        while (current != null) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ignored) {
                current = current.getSuperclass();
            }
        }
        throw new NoSuchFieldException(fieldName);
    }

    @BeforeEach
    void setup() {
        // ordem importa por causa de FK
        pedidoRepository.deleteAll();
        carrinhoRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    @Test
    void deveCriarPedidoPersistidoAPartirDeCarrinhoComItens() {

        UsuarioEntity usuario = new UsuarioEntity();
        setField(usuario, "nome", "Usuario Teste");
        setField(usuario, "email", "user.teste01@email.com");
        setField(usuario, "whatsapp", "99 99999-9999");
        setField(usuario, "cpf", "12245678201");
        setField(usuario, "senha", "Senha@123"); // aqui pode ser qualquer string

        UsuarioEntity usuarioSalvo = usuarioRepository.saveAndFlush(usuario);

        CarrinhoEntity carrinho = new CarrinhoEntity(usuarioSalvo);
        carrinho.adicionarItem(new ItemCarrinhoEntity(10L, "mouse gamer", new BigDecimal("150.00"), 2));
        carrinho.adicionarItem(new ItemCarrinhoEntity(11L, "teclado", new BigDecimal("150.00"), 2));

        carrinhoRepository.saveAndFlush(carrinho);

        PedidoEntity pedido = pedidoService.criarPedidoDoCarrinho(usuarioSalvo.getEmail());

        assertNotNull(pedido.getId());
        assertEquals(2, pedido.getItens().size());
        assertEquals(new BigDecimal("600.00"), pedido.getTotal().setScale(2));
        assertEquals("CRIADO", pedido.getStatus().name());
        assertNotNull(pedido.getCriadoEm());

        PedidoEntity encontrado = pedidoRepository.findById(pedido.getId()).orElseThrow();
        assertEquals(new BigDecimal("600.00"), encontrado.getTotal().setScale(2));
        assertEquals(2, encontrado.getItens().size());
    }
}