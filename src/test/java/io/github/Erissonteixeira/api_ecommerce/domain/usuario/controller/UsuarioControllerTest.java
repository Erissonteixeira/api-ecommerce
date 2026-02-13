package io.github.Erissonteixeira.api_ecommerce.domain.usuario.controller;

import io.github.Erissonteixeira.api_ecommerce.domain.usuario.dto.UsuarioRequestDto;
import io.github.Erissonteixeira.api_ecommerce.domain.usuario.dto.UsuarioResponseDto;
import io.github.Erissonteixeira.api_ecommerce.domain.usuario.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @Test
    void deveCriarUsuarioEretornar201() throws Exception {
        UsuarioResponseDto response = new UsuarioResponseDto();
        response.setId(1L);
        response.setNome("Erisson Teixeira");
        response.setEmail("erisson@dominio.com.br");
        response.setWhatsapp("51 99999-9999");
        response.setCpf("12345678909");
        response.setCriadoEm(LocalDateTime.now());

        when(usuarioService.criar(any(UsuarioRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "Erisson Teixeira",
                                  "email": "erisson@dominio.com.br",
                                  "whatsapp": "51 99999-9999",
                                  "cpf": "123.456.789-09",
                                  "senha": "Senha@123"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("erisson@dominio.com.br"))
                .andExpect(jsonPath("$.cpf").value("12345678909"));
    }

    @Test
    void deveRetornar400QuandoBodyInvalido() throws Exception {
        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "E",
                                  "email": "emailerrado",
                                  "whatsapp": "xxx",
                                  "cpf": "111",
                                  "senha": "123"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveBuscarPorId() throws Exception {
        UsuarioResponseDto response = new UsuarioResponseDto();
        response.setId(1L);
        response.setNome("Erisson Teixeira");
        response.setEmail("erisson@dominio.com.br");
        response.setWhatsapp("51 99999-9999");
        response.setCpf("12345678909");
        response.setCriadoEm(LocalDateTime.now());

        when(usuarioService.buscarPorId(eq(1L))).thenReturn(response);

        mockMvc.perform(get("/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deveRemoverERetornar204() throws Exception {
        mockMvc.perform(delete("/usuarios/1"))
                .andExpect(status().isNoContent());
    }
}
