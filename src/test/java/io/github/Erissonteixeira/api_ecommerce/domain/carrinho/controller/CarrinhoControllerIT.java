package io.github.Erissonteixeira.api_ecommerce.domain.carrinho.controller;

import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.repository.CarrinhoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CarrinhoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CarrinhoRepository carrinhoRepository;

    @BeforeEach
    void setup() {
        carrinhoRepository.deleteAll();
    }

    @Test
    void deveCriarCarrinhoEBuscar() throws Exception {

        String response = mockMvc.perform(post("/carrinhos"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.itens", is(empty())))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = extrairId(response);

        mockMvc.perform(get("/carrinhos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.itens", isA(java.util.List.class)));
    }

    @Test
    void deveAdicionarDoisItensECalcularTotal() throws Exception {
        String responseCriacao = mockMvc.perform(post("/carrinhos"))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long carrinhoId = extrairId(responseCriacao);

        mockMvc.perform(post("/carrinhos/{id}/itens", carrinhoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "produtoId": 10,
                                  "nomeProduto": "mouse gamer",
                                  "preco": 150.00,
                                  "quantidade": 2
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itens", hasSize(1)))
                .andExpect(jsonPath("$.total").value(300.00));

        mockMvc.perform(post("/carrinhos/{id}/itens", carrinhoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "produtoId": 11,
                                  "nomeProduto": "teclado mecanico",
                                  "preco": 150.00,
                                  "quantidade": 2
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itens", hasSize(2)))
                .andExpect(jsonPath("$.total").value(600.00));

        mockMvc.perform(get("/carrinhos/{id}/total", carrinhoId))
                .andExpect(status().isOk())
                .andExpect(content().string("600.00"));
    }

    @Test
    void deveRemoverItemAteSumir() throws Exception {
        String responseCriacao = mockMvc.perform(post("/carrinhos"))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long carrinhoId = extrairId(responseCriacao);

        mockMvc.perform(post("/carrinhos/{id}/itens", carrinhoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "produtoId": 10,
                                  "nomeProduto": "mouse gamer",
                                  "preco": 150.00,
                                  "quantidade": 2
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itens[0].quantidade").value(2));

        mockMvc.perform(delete("/carrinhos/{id}/itens/{produtoId}", carrinhoId, 10))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/carrinhos/{id}", carrinhoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itens", hasSize(1)))
                .andExpect(jsonPath("$.itens[0].quantidade").value(1));

        mockMvc.perform(delete("/carrinhos/{id}/itens/{produtoId}", carrinhoId, 10))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/carrinhos/{id}", carrinhoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itens", hasSize(0)))
                .andExpect(jsonPath("$.total").value(0.00));
    }

    private Long extrairId(String json) {
        int idx = json.indexOf("\"id\"");
        int colon = json.indexOf(":", idx);
        int comma = json.indexOf(",", colon);
        String idStr = json.substring(colon + 1, comma).trim();
        return Long.valueOf(idStr);
    }
}