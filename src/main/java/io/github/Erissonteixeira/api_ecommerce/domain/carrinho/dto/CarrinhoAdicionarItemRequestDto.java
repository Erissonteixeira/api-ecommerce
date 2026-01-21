package io.github.Erissonteixeira.api_ecommerce.domain.carrinho.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CarrinhoAdicionarItemRequestDto {

    @NotNull
    private Long produtoId;

    @NotBlank
    private String nomeProduto;

    @NotNull
    @Positive
    private BigDecimal preco;

    @NotNull
    @Positive
    private Integer quantidade;
}
