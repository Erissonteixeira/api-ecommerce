package io.github.Erissonteixeira.api_ecommerce.domain.carrinho.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.Erissonteixeira.api_ecommerce.domain.dto.CarrinhoAdicionarItemRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CarrinhoControllerValidationIT {

    private static final long ID_INEXISTENTE = 999999L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveRetornar404_quandoBuscarCarrinhoInexistente() throws Exception {
        mockMvc.perform(get("/carrinhos/{carrinhoId}", ID_INEXISTENTE))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornar404_quandoAdicionarItemEmCarrinhoInexistente() throws Exception {
        CarrinhoAdicionarItemRequestDto dto = dtoValido();

        mockMvc.perform(post("/carrinhos/{carrinhoId}/itens", ID_INEXISTENTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornar404_quandoCalcularTotalDeCarrinhoInexistente() throws Exception {
        mockMvc.perform(get("/carrinhos/{carrinhoId}/total", ID_INEXISTENTE))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornar400_quandoAdicionarItemComProdutoIdNulo() throws Exception {
        Long carrinhoId = criarCarrinhoEObterId();
        CarrinhoAdicionarItemRequestDto dto = dtoValido();
        dto.setProdutoId(null);

        mockMvc.perform(post("/carrinhos/{carrinhoId}/itens", carrinhoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400_quandoAdicionarItemComNomeEmBranco() throws Exception {
        Long carrinhoId = criarCarrinhoEObterId();
        CarrinhoAdicionarItemRequestDto dto = dtoValido();
        dto.setNomeProduto("   ");

        mockMvc.perform(post("/carrinhos/{carrinhoId}/itens", carrinhoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400_quandoAdicionarItemComPrecoNulo() throws Exception {
        Long carrinhoId = criarCarrinhoEObterId();
        CarrinhoAdicionarItemRequestDto dto = dtoValido();
        dto.setPreco(null);

        mockMvc.perform(post("/carrinhos/{carrinhoId}/itens", carrinhoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400_quandoAdicionarItemComPrecoZeroOuNegativo() throws Exception {
        Long carrinhoId = criarCarrinhoEObterId();

        CarrinhoAdicionarItemRequestDto dtoZero = dtoValido();
        dtoZero.setPreco(BigDecimal.ZERO);

        mockMvc.perform(post("/carrinhos/{carrinhoId}/itens", carrinhoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoZero)))
                .andExpect(status().isBadRequest());

        CarrinhoAdicionarItemRequestDto dtoNeg = dtoValido();
        dtoNeg.setPreco(new BigDecimal("-1"));

        mockMvc.perform(post("/carrinhos/{carrinhoId}/itens", carrinhoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoNeg)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400_quandoAdicionarItemComQuantidadeNulaOuZero() throws Exception {
        Long carrinhoId = criarCarrinhoEObterId();

        CarrinhoAdicionarItemRequestDto dtoNula = dtoValido();
        dtoNula.setQuantidade(null);

        mockMvc.perform(post("/carrinhos/{carrinhoId}/itens", carrinhoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoNula)))
                .andExpect(status().isBadRequest());

        CarrinhoAdicionarItemRequestDto dtoZero = dtoValido();
        dtoZero.setQuantidade(0);

        mockMvc.perform(post("/carrinhos/{carrinhoId}/itens", carrinhoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoZero)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400_quandoJsonForInvalido() throws Exception {
        Long carrinhoId = criarCarrinhoEObterId();

        mockMvc.perform(post("/carrinhos/{carrinhoId}/itens", carrinhoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{"))
                .andExpect(status().isBadRequest());
    }

    private CarrinhoAdicionarItemRequestDto dtoValido() {
        CarrinhoAdicionarItemRequestDto dto = new CarrinhoAdicionarItemRequestDto();
        dto.setProdutoId(10L);
        dto.setNomeProduto("Produto Teste");
        dto.setPreco(new BigDecimal("50.00"));
        dto.setQuantidade(2);
        return dto;
    }

    private Long criarCarrinhoEObterId() throws Exception {
        String json = mockMvc.perform(post("/carrinhos"))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(json).get("id").asLong();
    }
}
