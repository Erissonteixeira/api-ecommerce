package io.github.Erissonteixeira.api_ecommerce.domain.produto;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.Erissonteixeira.api_ecommerce.domain.produto.entity.ProdutoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.produto.entity.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

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
}