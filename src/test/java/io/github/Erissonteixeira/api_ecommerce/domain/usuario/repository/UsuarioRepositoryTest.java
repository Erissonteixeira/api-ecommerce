package io.github.Erissonteixeira.api_ecommerce.domain.usuario.repository;

import io.github.Erissonteixeira.api_ecommerce.domain.usuario.entity.UsuarioEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository repository;

    private UsuarioEntity novoUsuario(String nome, String email, String cpf) {
        UsuarioEntity u = new UsuarioEntity();
        u.setNome(nome);
        u.setEmail(email);
        u.setWhatsapp("51999999999");
        u.setCpf(cpf);
        u.setSenha("Senha@123");
        return u;
    }

    @Test
    void existsByEmail_deveRetornarTrueQuandoExistir() {
        repository.saveAndFlush(novoUsuario("Erisson", "existe@email.com", "12345678901"));

        assertTrue(repository.existsByEmail("existe@email.com"));
        assertFalse(repository.existsByEmail("naoexiste@email.com"));
    }

    @Test
    void existsByCpf_deveRetornarTrueQuandoExistir() {
        repository.saveAndFlush(novoUsuario("Erisson", "ok@email.com", "12345678901"));

        assertTrue(repository.existsByCpf("12345678901"));
        assertFalse(repository.existsByCpf("99999999999"));
    }

    @Test
    void findByEmail_deveRetornarUsuarioQuandoExistir() {
        repository.saveAndFlush(novoUsuario("Erisson", "busca@email.com", "12345678901"));

        Optional<UsuarioEntity> encontrado = repository.findByEmail("busca@email.com");

        assertTrue(encontrado.isPresent());
        assertEquals("busca@email.com", encontrado.get().getEmail());
    }

    @Test
    void findByEmail_deveRetornarVazioQuandoNaoExistir() {
        Optional<UsuarioEntity> encontrado = repository.findByEmail("naoexiste@email.com");
        assertTrue(encontrado.isEmpty());
    }

    @Test
    void naoDevePermitirEmailDuplicadoNoBanco() {
        repository.saveAndFlush(novoUsuario("Erisson", "dup@email.com", "12345678901"));

        assertThrows(DataIntegrityViolationException.class, () -> {
            repository.saveAndFlush(novoUsuario("Outro", "dup@email.com", "12345678902"));
        });
    }

    @Test
    void naoDevePermitirCpfDuplicadoNoBanco() {
        repository.saveAndFlush(novoUsuario("Erisson", "a@email.com", "12345678901"));

        assertThrows(DataIntegrityViolationException.class, () -> {
            repository.saveAndFlush(novoUsuario("Outro", "b@email.com", "12345678901"));
        });
    }
}
