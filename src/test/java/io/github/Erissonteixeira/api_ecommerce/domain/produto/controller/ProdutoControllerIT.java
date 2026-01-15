package io.github.Erissonteixeira.api_ecommerce.domain.produto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.Erissonteixeira.api_ecommerce.domain.produto.dto.ProdutoRequestDto;
import io.github.Erissonteixeira.api_ecommerce.domain.produto.entity.ProdutoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.produto.entity.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ProdutoControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProdutoRepository produtoRepository;

    @BeforeEach
    void setup() {
        produtoRepository.deleteAll();
    }

    private ProdutoEntity novoProduto(String nome, String preco, boolean ativo) {
        ProdutoEntity p = new ProdutoEntity();
        p.setNome(nome);
        p.setPreco(new BigDecimal(preco));
        p.setAtivo(ativo);
        return p;
    }

    @Test
    void deveCriarProduto() throws Exception {
        ProdutoRequestDto dto = new ProdutoRequestDto();
        dto.setNome("Teclado Mecânico");
        dto.setPreco(new BigDecimal("199.90"));
        dto.setAtivo(true);

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.nome").value("Teclado Mecânico"))
                .andExpect(jsonPath("$.preco").value(199.90))
                .andExpect(jsonPath("$.ativo").value(true))
                .andExpect(jsonPath("$.criadoEm", notNullValue()))
                .andExpect(jsonPath("$.atualizadoEm").doesNotExist());
    }

    @Test
    void deveBuscarPorId_quandoExiste() throws Exception {
        ProdutoEntity salvo = produtoRepository.save(novoProduto("Mouse Gamer", "99.90", true));

        mockMvc.perform(get("/produtos/{id}", salvo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(salvo.getId()))
                .andExpect(jsonPath("$.nome").value("Mouse Gamer"))
                .andExpect(jsonPath("$.preco").value(99.90))
                .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    void deveListarTodos_quandoAtivosFalseOuNaoInformado() throws Exception {
        produtoRepository.save(novoProduto("A", "10.00", true));
        produtoRepository.save(novoProduto("B", "20.00", false));

        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        mockMvc.perform(get("/produtos").param("ativos", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void deveListarSomenteAtivos_quandoAtivosTrue() throws Exception {
        produtoRepository.save(novoProduto("Ativo", "10.00", true));
        produtoRepository.save(novoProduto("Inativo", "20.00", false));

        mockMvc.perform(get("/produtos").param("ativos", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nome").value("Ativo"))
                .andExpect(jsonPath("$[0].ativo").value(true));
    }

    @Test
    void deveAtualizarProduto() throws Exception {
        ProdutoEntity salvo = produtoRepository.save(novoProduto("Monitor 24", "799.00", true));

        ProdutoRequestDto dto = new ProdutoRequestDto();
        dto.setNome("Monitor 27");
        dto.setPreco(new BigDecimal("999.00"));
        dto.setAtivo(true);

        mockMvc.perform(put("/produtos/{id}", salvo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(salvo.getId()))
                .andExpect(jsonPath("$.nome").value("Monitor 27"))
                .andExpect(jsonPath("$.preco").value(999.00))
                .andExpect(jsonPath("$.ativo").value(true))
                .andExpect(jsonPath("$.atualizadoEm", notNullValue()));
    }

    @Test
    void deveDesativarProduto_eRetornar204() throws Exception {
        ProdutoEntity salvo = produtoRepository.save(novoProduto("SSD 1TB", "399.90", true));

        mockMvc.perform(delete("/produtos/{id}", salvo.getId()))
                .andExpect(status().isNoContent());

        ProdutoEntity atualizado = produtoRepository.findById(salvo.getId()).orElseThrow();
        org.junit.jupiter.api.Assertions.assertFalse(atualizado.getAtivo());
        org.junit.jupiter.api.Assertions.assertNotNull(atualizado.getAtualizadoEm());
    }
}
