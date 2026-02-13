package io.github.Erissonteixeira.api_ecommerce.domain.usuario.entity;

import io.github.Erissonteixeira.api_ecommerce.domain.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UsuarioEntityTest {

    @Autowired
    private UsuarioRepository repository;

    @Test
    void devePreencherCriadoEmEAtualizadoEmNoPrePersist() {
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setNome("Erisson");
        usuario.setEmail("teste@email.com");
        usuario.setWhatsapp("51999999999");
        usuario.setCpf("12345678901");
        usuario.setSenha("123");

        UsuarioEntity salvo = repository.saveAndFlush(usuario);

        assertNotNull(salvo.getCriadoEm());
        assertNull(salvo.getAtualizadoEm());
    }

    @Test
    void devePreencherAtualizadoEmNoPreUpdate() {
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setNome("Erisson");
        usuario.setEmail("teste2@email.com");
        usuario.setWhatsapp("51999999999");
        usuario.setCpf("12345678902");
        usuario.setSenha("123");

        UsuarioEntity salvo = repository.saveAndFlush(usuario);

        salvo.setNome("Novo Nome");
        UsuarioEntity atualizado = repository.saveAndFlush(salvo);

        assertNotNull(atualizado.getAtualizadoEm());
    }

    @Test
    void deveManterCriadoEmImutavelAoAtualizar() {
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setNome("Erisson");
        usuario.setEmail("teste3@email.com");
        usuario.setWhatsapp("51999999999");
        usuario.setCpf("12345678903");
        usuario.setSenha("123");

        UsuarioEntity salvo = repository.saveAndFlush(usuario);
        var criadoEmOriginal = salvo.getCriadoEm();

        salvo.setNome("Outro Nome");
        UsuarioEntity atualizado = repository.saveAndFlush(salvo);

        assertEquals(criadoEmOriginal, atualizado.getCriadoEm());
        assertNotNull(atualizado.getAtualizadoEm());
    }
}
