package io.github.Erissonteixeira.api_ecommerce.domain.usuario.service;

import io.github.Erissonteixeira.api_ecommerce.domain.usuario.dto.UsuarioRequestDto;
import io.github.Erissonteixeira.api_ecommerce.domain.usuario.repository.UsuarioRepository;
import io.github.Erissonteixeira.api_ecommerce.exception.NegocioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
class UsuarioServiceImplTest {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    UsuarioRepository usuarioRepository;

    @BeforeEach
    void setup() {
        usuarioRepository.deleteAll();
    }

    private UsuarioRequestDto dtoValido(String email, String cpf) {
        UsuarioRequestDto dto = new UsuarioRequestDto();
        dto.setNome("Erisson Teixeira");
        dto.setEmail(email);
        dto.setWhatsapp("51 99999-9999");
        dto.setCpf(cpf);
        dto.setSenha("Senha@123");
        return dto;
    }

    @Test
    void deveCriarUsuarioETransformarCpfEmSomenteNumeros() {
        var resp = usuarioService.criar(dtoValido("ERISSON@DOMINIO.COM.BR", "123.456.789-09"));

        assertNotNull(resp.getId());
        assertEquals("erisson@dominio.com.br", resp.getEmail());
        assertEquals("12345678909", resp.getCpf());
    }

    @Test
    void naoDevePermitirEmailDuplicado() {
        usuarioService.criar(dtoValido("a@a.com", "123.456.789-09"));

        NegocioException ex = assertThrows(NegocioException.class,
                () -> usuarioService.criar(dtoValido("a@a.com", "987.654.321-00")));

        assertTrue(ex.getMessage().toLowerCase().contains("e-mail"));
    }

    @Test
    void naoDevePermitirCpfDuplicado() {
        usuarioService.criar(dtoValido("a@a.com", "123.456.789-09"));

        NegocioException ex = assertThrows(NegocioException.class,
                () -> usuarioService.criar(dtoValido("b@b.com", "12345678909")));

        assertTrue(ex.getMessage().toLowerCase().contains("cpf"));
    }

    @Test
    void deveAtualizarUsuario() {
        var criado = usuarioService.criar(dtoValido("a@a.com", "123.456.789-09"));

        UsuarioRequestDto novo = dtoValido("b@b.com", "987.654.321-00");
        novo.setNome("Erisson T.");

        var atualizado = usuarioService.atualizar(criado.getId(), novo);

        assertEquals("Erisson T.", atualizado.getNome());
        assertEquals("b@b.com", atualizado.getEmail());
        assertEquals("98765432100", atualizado.getCpf());
    }

    @Test
    void deveRemoverUsuario() {
        var criado = usuarioService.criar(dtoValido("a@a.com", "123.456.789-09"));

        usuarioService.remover(criado.getId());

        assertTrue(usuarioRepository.findById(criado.getId()).isEmpty());
    }
}
