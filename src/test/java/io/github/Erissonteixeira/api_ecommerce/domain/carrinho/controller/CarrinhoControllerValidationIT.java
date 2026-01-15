package io.github.Erissonteixeira.api_ecommerce.domain.carrinho.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CarrinhoControllerValidationIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveRetornar404_quandoBuscarCarrinhoInexistente() throws Exception {
        mockMvc.perform(get("/carrinhos/{carrinhoId}", 999999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornar404_quandoAdicionarItemEmCarrinhoInexistente() throws Exception {
        CarrinhoAdicionarItemRequestDto dto = dtoValido();

        mockMvc.perform(post("/carrinhos/{carrinhoId}/itens", 999999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }
