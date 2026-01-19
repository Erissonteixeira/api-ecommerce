package io.github.Erissonteixeira.api_ecommerce.domain.pedido.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity.CarrinhoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity.ItemCarrinhoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.repository.CarrinhoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class PedidoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CarrinhoRepository carrinhoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void criarPedido_quandoCarrinhoExiste_deveRetornar201ComPedido() throws Exception {

        CarrinhoEntity carrinho = new CarrinhoEntity();

        ItemCarrinhoEntity item = new ItemCarrinhoEntity(
                10L,
                "mouse gamer",
                new BigDecimal("150.00"),
                2
        );
        carrinho.adicionarItem(item);

        carrinho = carrinhoRepository.save(carrinho);

        mockMvc.perform(
                        post("/carrinhos/{id}/pedido", carrinho.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.status").value("CRIADO"))
                .andExpect(jsonPath("$.total").value(300.00))
                .andExpect(jsonPath("$.itens").isArray())
                .andExpect(jsonPath("$.itens.length()").value(1))
                .andExpect(jsonPath("$.itens[0].produtoId").value(10))
                .andExpect(jsonPath("$.itens[0].subtotal").value(300.00));
    }

    @Test
    void criarPedido_quandoCarrinhoNaoExiste_deveRetornar404() throws Exception {
        mockMvc.perform(
                        post("/carrinhos/{id}/pedido", 999L)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }
}
