package io.github.Erissonteixeira.api_ecommerce.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ProdutoResponseDto {

    private Long id;
    private String nome;
    private BigDecimal preco;
    private Boolean ativo;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
