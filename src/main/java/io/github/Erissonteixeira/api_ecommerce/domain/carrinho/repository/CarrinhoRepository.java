package io.github.Erissonteixeira.api_ecommerce.domain.carrinho.repository;

import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity.CarrinhoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarrinhoRepository extends JpaRepository<CarrinhoEntity, Long> {
    @Query("select c from CarrinhoEntity c left join fetch c.itens where c.id = :id")
    Optional<CarrinhoEntity> buscarComItensPorId(Long id);
}
