package io.github.Erissonteixeira.api_ecommerce.domain.pedido.repository;

import io.github.Erissonteixeira.api_ecommerce.domain.pedido.entity.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<PedidoEntity, Long> {

    List<PedidoEntity> findAllByUsuarioIdOrderByCriadoEmDesc(Long usuarioId);

    Optional<PedidoEntity> findByIdAndUsuarioId(Long id, Long usuarioId);
}