package io.github.Erissonteixeira.api_ecommerce.domain.produto.repository;

import io.github.Erissonteixeira.api_ecommerce.domain.produto.entity.ProdutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<ProdutoEntity, Long> {

    List<ProdutoEntity> findAllByAtivoTrue();

    Optional<ProdutoEntity> findByIdAndAtivoTrue(Long id);
}