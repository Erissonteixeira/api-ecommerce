package io.github.Erissonteixeira.api_ecommerce.domain.pedido.repository;

import io.github.Erissonteixeira.api_ecommerce.domain.pedido.entity.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<PedidoEntity, Long> {

    @Query("""
            select distinct p
            from PedidoEntity p
            left join fetch p.usuario
            left join fetch p.itens
            where p.usuario.id = :usuarioId
            order by p.criadoEm desc
            """)
    List<PedidoEntity> findAllByUsuarioIdOrderByCriadoEmDesc(Long usuarioId);

    @Query("""
            select distinct p
            from PedidoEntity p
            left join fetch p.usuario
            left join fetch p.itens
            where p.id = :id
              and p.usuario.id = :usuarioId
            """)
    Optional<PedidoEntity> findByIdAndUsuarioId(Long id, Long usuarioId);
}