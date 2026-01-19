package io.github.Erissonteixeira.api_ecommerce.domain.pedido.repository;

import io.github.Erissonteixeira.api_ecommerce.domain.pedido.entity.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<PedidoEntity, Long> {
}
