package io.github.Erissonteixeira.api_ecommerce.domain.carrinho.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.dto.CarrinhoAdicionarItemRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CarrinhoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarCarrinho_com201EBodyComId() throws Exception {
        String json = mockMvc.perform(post("/carrinhos"))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.itens").isArray())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = objectMapper.readTree(json).get("id").asLong();
        assertThat(id).isPositive();
    }

    @Test
    void deveAdicionarItemECalcularTotal() throws Exception {
        Long carrinhoId = criarCarrinhoEObterId();

        CarrinhoAdicionarItemRequestDto dto = new CarrinhoAdicionarItemRequestDto();
        dto.setProdutoId(10L);
        dto.setNomeProduto("Produto Teste");
        dto.setPreco(new BigDecimal("50.00"));
        dto.setQuantidade(2);

        mockMvc.perform(post("/carrinhos/{carrinhoId}/itens", carrinhoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(carrinhoId))
                .andExpect(jsonPath("$.itens.length()").value(1))
                .andExpect(jsonPath("$.itens[0].produtoId").value(10))
                .andExpect(jsonPath("$.itens[0].quantidade").value(2))
                .andExpect(jsonPath("$.itens[0].precoUnitario", closeTo(50.0, 0.0001)))
                .andExpect(jsonPath("$.itens[0].subtotal", closeTo(100.0, 0.0001)))
                .andExpect(jsonPath("$.total", closeTo(100.0, 0.0001)));

        String totalStr = mockMvc.perform(get("/carrinhos/{carrinhoId}/total", carrinhoId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        BigDecimal total = new BigDecimal(totalStr);
        assertThat(total).isEqualByComparingTo(new BigDecimal("100.00"));
    }

    @Test
    void deveIncrementarQuantidadeQuandoAdicionarMesmoProduto() throws Exception {
        Long carrinhoId = criarCarrinhoEObterId();

        CarrinhoAdicionarItemRequestDto dto1 = new CarrinhoAdicionarItemRequestDto();
        dto1.setProdutoId(10L);
        dto1.setNomeProduto("Produto Teste");
        dto1.setPreco(new BigDecimal("50.00"));
        dto1.setQuantidade(1);

        CarrinhoAdicionarItemRequestDto dto2 = new CarrinhoAdicionarItemRequestDto();
        dto2.setProdutoId(10L);
        dto2.setNomeProduto("Produto Teste");
        dto2.setPreco(new BigDecimal("50.00"));
        dto2.setQuantidade(2);

        mockMvc.perform(post("/carrinhos/{carrinhoId}/itens", carrinhoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto1)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/carrinhos/{carrinhoId}/itens", carrinhoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itens.length()").value(1))
                .andExpect(jsonPath("$.itens[0].quantidade").value(3))
                .andExpect(jsonPath("$.total", closeTo(150.0, 0.0001)));
    }

    @Test
    void deveRemoverItem_decrementandoQuandoQuantidadeMaiorQue1_eRemoverDefinitivoQuandoChegarEm1() throws Exception {
        Long carrinhoId = criarCarrinhoEObterId();

        CarrinhoAdicionarItemRequestDto dto = new CarrinhoAdicionarItemRequestDto();
        dto.setProdutoId(10L);
        dto.setNomeProduto("Produto Teste");
        dto.setPreco(new BigDecimal("50.00"));
        dto.setQuantidade(2);

        mockMvc.perform(post("/carrinhos/{carrinhoId}/itens", carrinhoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/carrinhos/{carrinhoId}/itens/{produtoId}", carrinhoId, 10L))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/carrinhos/{carrinhoId}", carrinhoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itens.length()").value(1))
                .andExpect(jsonPath("$.itens[0].quantidade").value(1))
                .andExpect(jsonPath("$.total", closeTo(50.0, 0.0001)));

        mockMvc.perform(delete("/carrinhos/{carrinhoId}/itens/{produtoId}", carrinhoId, 10L))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/carrinhos/{carrinhoId}", carrinhoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itens.length()").value(0))
                .andExpect(jsonPath("$.total").value(0)); // <-- AQUI
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
