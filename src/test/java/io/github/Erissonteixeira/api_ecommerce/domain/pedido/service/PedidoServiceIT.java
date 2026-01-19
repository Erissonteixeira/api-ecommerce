package io.github.Erissonteixeira.api_ecommerce.domain.pedido.service;

import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity.CarrinhoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity.ItemCarrinhoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.repository.CarrinhoRepository;
import io.github.Erissonteixeira.api_ecommerce.domain.pedido.entity.PedidoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.pedido.repository.PedidoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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

    @Test
    void deveCriarPedidoPersistidoAPartirDeCarrinhoComItens() {

        CarrinhoEntity carrinho = new CarrinhoEntity();
        carrinho.adicionarItem(new ItemCarrinhoEntity(10L, "mouse gamer", new BigDecimal("150.00"), 2)); // 300
        carrinho.adicionarItem(new ItemCarrinhoEntity(11L, "teclado", new BigDecimal("150.00"), 2));    // 300

        CarrinhoEntity salvo = carrinhoRepository.save(carrinho);

        PedidoEntity pedido = pedidoService.criarPedidoDoCarrinho(salvo.getId());

        assertNotNull(pedido.getId());
        assertEquals(2, pedido.getItens().size());
        assertEquals(new BigDecimal("600.00"), pedido.getTotal());

        PedidoEntity encontrado = pedidoRepository.findById(pedido.getId()).orElseThrow();
        assertEquals(new BigDecimal("600.00"), encontrado.getTotal());
        assertEquals(2, encontrado.getItens().size());
    }
}
