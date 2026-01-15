package io.github.Erissonteixeira.api_ecommerce.domain.produto.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProdutoControllerValidationIT {

    private static final String JSON_VALIDO = """
            {
              "nome": "Produto Teste",
              "preco": 10.50,
              "ativo": true
            }
            """;
    private static final String JSON_NOME_EM_BRANCO = """
            {
              "nome": "   ",
              "preco": 10.50,
              "ativo": true
            }
            """;
    private static final String JSON_NOME_NULO = """
            {
              "nome": null,
              "preco": 10.50,
              "ativo": true
            }
            """;
    private static final String JSON_PRECO_NULO = """
            {
              "nome": "Produto Teste",
              "preco": null,
              "ativo": true
            }
            """;
    private static final String JSON_PRECO_ZERO = """
            {
              "nome": "Produto Teste",
              "preco": 0,
              "ativo": true
            }
            """;
    private static final String JSON_PRECO_NEGATIVO = """
            {
              "nome": "Produto Teste",
              "preco": -1,
              "ativo": true
            }
            """;
    private static final String JSON_ATIVO_NULO = """
            {
              "nome": "Produto Teste",
              "preco": 10.50,
              "ativo": null
            }
            """;
    private static final String JSON_FALTANDO_CAMPO_ATIVO = """
            {
              "nome": "Produto Teste",
              "preco": 10.50
            }
            """;
    private static final String JSON_FALTANDO_CAMPO_NOME = """
            {
              "preco": 10.50,
              "ativo": true
            }
            """;
    private static final String JSON_FALTANDO_CAMPO_PRECO = """
            {
              "nome": "Produto Teste",
              "ativo": true
            }
            """;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveRetornar400_quandoCriarComNomeEmBranco() throws Exception {
        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_NOME_EM_BRANCO))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400_quandoCriarComNomeNulo() throws Exception {
        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_NOME_NULO))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400_quandoCriarComPrecoNulo() throws Exception {
        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_PRECO_NULO))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400_quandoCriarComPrecoZero() throws Exception {
        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_PRECO_ZERO))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400_quandoCriarComPrecoNegativo() throws Exception {
        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_PRECO_NEGATIVO))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400_quandoCriarComAtivoNulo() throws Exception {
        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_ATIVO_NULO))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400_quandoCriarSemCampoAtivo() throws Exception {
        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_FALTANDO_CAMPO_ATIVO))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400_quandoCriarSemCampoNome() throws Exception {
        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_FALTANDO_CAMPO_NOME))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400_quandoCriarSemCampoPreco() throws Exception {
        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_FALTANDO_CAMPO_PRECO))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400_quandoAtualizarComNomeEmBranco() throws Exception {
        mockMvc.perform(put("/produtos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_NOME_EM_BRANCO))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400_quandoAtualizarComPrecoNegativo() throws Exception {
        mockMvc.perform(put("/produtos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_PRECO_NEGATIVO))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400_quandoAtualizarSemCampoAtivo() throws Exception {
        mockMvc.perform(put("/produtos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_FALTANDO_CAMPO_ATIVO))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400_quandoAtualizarSemCampoPreco() throws Exception {
        mockMvc.perform(put("/produtos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_FALTANDO_CAMPO_PRECO))
                .andExpect(status().isBadRequest());
    }
}
