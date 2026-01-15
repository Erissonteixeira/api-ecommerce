package io.github.Erissonteixeira.api_ecommerce.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CarrinhoResponseDto {
    private Long id;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
    private List<CarrinhoItemResponseDto> itens;
    private BigDecimal total;
}
