package io.github.Erissonteixeira.api_ecommerce.domain.usuario.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.Erissonteixeira.api_ecommerce.domain.auth.security.JwtAuthFilter;
import io.github.Erissonteixeira.api_ecommerce.domain.auth.security.JwtService;
import io.github.Erissonteixeira.api_ecommerce.domain.usuario.dto.UsuarioRequestDto;
import io.github.Erissonteixeira.api_ecommerce.domain.usuario.dto.UsuarioResponseDto;
import io.github.Erissonteixeira.api_ecommerce.domain.usuario.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UsuarioService usuarioService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    void deveRetornar400QuandoBodyInvalido() throws Exception {
        UsuarioRequestDto dto = new UsuarioRequestDto();

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveBuscarPorId() throws Exception {
        Long id = 1L;

        UsuarioResponseDto response = new UsuarioResponseDto();
        response.setId(id);
        response.setNome("Erisson");
        response.setEmail("erisson@email.com");

        when(usuarioService.buscarPorId(id)).thenReturn(response);

        mockMvc.perform(get("/usuarios/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nome").value("Erisson"))
                .andExpect(jsonPath("$.email").value("erisson@email.com"));
    }

    @Test
    void deveListar() throws Exception {
        UsuarioResponseDto u1 = new UsuarioResponseDto();
        u1.setId(1L);
        u1.setNome("A");
        u1.setEmail("a@email.com");

        UsuarioResponseDto u2 = new UsuarioResponseDto();
        u2.setId(2L);
        u2.setNome("B");
        u2.setEmail("b@email.com");

        when(usuarioService.listar()).thenReturn(List.of(u1, u2));

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void deveCriar() throws Exception {
        UsuarioRequestDto request = new UsuarioRequestDto();
        request.setNome("Novo");
        request.setEmail("novo@email.com");
        request.setWhatsapp("51 99999-9999");
        request.setCpf("12345678901");
        request.setSenha("Senha@123");

        UsuarioResponseDto response = new UsuarioResponseDto();
        response.setId(10L);
        response.setNome("Novo");
        response.setEmail("novo@email.com");

        when(usuarioService.criar(any(UsuarioRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.nome").value("Novo"))
                .andExpect(jsonPath("$.email").value("novo@email.com"));
    }
}