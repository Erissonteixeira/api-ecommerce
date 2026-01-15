package io.github.Erissonteixeira.api_ecommerce.domain.produto;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
}