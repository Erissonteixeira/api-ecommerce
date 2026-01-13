package io.github.Erissonteixeira.api_ecommerce.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProdutoRequestDto {

    @NotBlank
    private String nome;

    @NotNull
    @Positive
    private BigDecimal preco;

    @NotNull
    private Boolean ativo;
}
