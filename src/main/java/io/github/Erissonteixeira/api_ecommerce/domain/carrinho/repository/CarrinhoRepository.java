package io.github.Erissonteixeira.api_ecommerce.domain.carrinho.repository;

import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity.CarrinhoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarrinhoRepository extends JpaRepository<CarrinhoEntity, Long> {
}
