package io.github.Erissonteixeira.api_ecommerce.domain.produto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

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

}
