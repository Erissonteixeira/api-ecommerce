package io.github.Erissonteixeira.api_ecommerce.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CarrinhoItemResponseDto {
    private Long produtoId;
    private String nomeProduto;
    private BigDecimal precoUnitario;
    private Integer quantidade;
    private BigDecimal subtotal;
}
