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

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CarrinhoControllerValidationIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CarrinhoRepository carrinhoRepository;

    @BeforeEach
    void setup() {
        carrinhoRepository.deleteAll();
    }

    @Test
    void adicionarItem_quandoNomeProdutoVazio_deveDar400() throws Exception {
        Long carrinhoId = criarCarrinho();

        mockMvc.perform(post("/carrinhos/{id}/itens", carrinhoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "produtoId": 10,
                                  "nomeProduto": "   ",
                                  "preco": 10.00,
                                  "quantidade": 1
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Dados inválidos"))
                .andExpect(jsonPath("$.fieldErrors", notNullValue()))
                .andExpect(jsonPath("$.fieldErrors[*].field", hasItem("nomeProduto")));
    }

    @Test
    void adicionarItem_quandoPrecoNulo_deveDar400() throws Exception {
        Long carrinhoId = criarCarrinho();

        mockMvc.perform(post("/carrinhos/{id}/itens", carrinhoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "produtoId": 10,
                                  "nomeProduto": "mouse",
                                  "preco": null,
                                  "quantidade": 1
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Dados inválidos"))
                .andExpect(jsonPath("$.fieldErrors[*].field", hasItem("preco")));
    }

    @Test
    void adicionarItem_quandoPrecoZeroOuNegativo_deveDar400() throws Exception {
        Long carrinhoId = criarCarrinho();

        mockMvc.perform(post("/carrinhos/{id}/itens", carrinhoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "produtoId": 10,
                                  "nomeProduto": "mouse",
                                  "preco": 0,
                                  "quantidade": 1
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[*].field", hasItem("preco")));

        mockMvc.perform(post("/carrinhos/{id}/itens", carrinhoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "produtoId": 10,
                                  "nomeProduto": "mouse",
                                  "preco": -1,
                                  "quantidade": 1
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[*].field", hasItem("preco")));
    }

    @Test
    void adicionarItem_quandoQuantidadeZero_deveDar400() throws Exception {
        Long carrinhoId = criarCarrinho();

        mockMvc.perform(post("/carrinhos/{id}/itens", carrinhoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "produtoId": 10,
                                  "nomeProduto": "mouse",
                                  "preco": 10.00,
                                  "quantidade": 0
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[*].field", hasItem("quantidade")));
    }

    private Long criarCarrinho() throws Exception {
        String response = mockMvc.perform(post("/carrinhos"))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        int idx = response.indexOf("\"id\"");
        int colon = response.indexOf(":", idx);
        int comma = response.indexOf(",", colon);
        return Long.valueOf(response.substring(colon + 1, comma).trim());
    }
}