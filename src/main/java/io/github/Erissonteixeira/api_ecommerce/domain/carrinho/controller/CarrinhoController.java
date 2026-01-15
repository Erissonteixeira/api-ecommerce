package io.github.Erissonteixeira.api_ecommerce.domain.carrinho.controller;

import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.mapper.CarrinhoMapper;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.service.CarrinhoService;
import io.github.Erissonteixeira.api_ecommerce.domain.dto.CarrinhoAdicionarItemRequestDto;
import io.github.Erissonteixeira.api_ecommerce.domain.dto.CarrinhoResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/carrinhos")
public class CarrinhoController {

    private final CarrinhoService carrinhoService;
    private final CarrinhoMapper carrinhoMapper;

    public CarrinhoController(CarrinhoService carrinhoService, CarrinhoMapper carrinhoMapper) {
        this.carrinhoService = carrinhoService;
        this.carrinhoMapper = carrinhoMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarrinhoResponseDto criar() {
        return carrinhoMapper.toResponse(carrinhoService.criarCarrinho());
    }

    @GetMapping("/{carrinhoId}")
    public CarrinhoResponseDto buscarPorId(@PathVariable Long carrinhoId) {
        return carrinhoMapper.toResponse(carrinhoService.buscarPorId(carrinhoId));
    }

    @PostMapping("/{carrinhoId}/itens")
    public CarrinhoResponseDto adicionarItem(
            @PathVariable Long carrinhoId,
            @RequestBody @Valid CarrinhoAdicionarItemRequestDto dto
    ) {
        return carrinhoMapper.toResponse(
                carrinhoService.adicionarItem(
                        carrinhoId,
                        dto.getProdutoId(),
                        dto.getNomeProduto(),
                        dto.getPreco(),
                        dto.getQuantidade()
                )
        );
    }

    @DeleteMapping("/{carrinhoId}/itens/{produtoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerItem(
            @PathVariable Long carrinhoId,
            @PathVariable Long produtoId
    ) {
        carrinhoService.removerItem(carrinhoId, produtoId);
    }

    @GetMapping("/{carrinhoId}/total")
    public BigDecimal calcularTotal(@PathVariable Long carrinhoId) {
        return carrinhoService.calcularTotal(carrinhoId);
    }
}
