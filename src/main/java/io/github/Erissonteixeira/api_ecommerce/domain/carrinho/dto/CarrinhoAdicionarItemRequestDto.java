package io.github.Erissonteixeira.api_ecommerce.domain.carrinho.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarrinhoAdicionarItemRequestDto {

    @NotNull
    private Long produtoId;

    @NotNull
    @Positive
    private Integer quantidade;
}