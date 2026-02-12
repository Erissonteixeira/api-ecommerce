package io.github.Erissonteixeira.api_ecommerce.domain.usuario.repository;

import io.github.Erissonteixeira.api_ecommerce.domain.usuario.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);

    Optional<UsuarioEntity> findByEmail(String email);
}
