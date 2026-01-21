package io.github.Erissonteixeira.api_ecommerce.domain.carrinho.controller;

import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.dto.CarrinhoAdicionarItemRequestDto;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.dto.CarrinhoResponseDto;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity.CarrinhoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.mapper.CarrinhoMapper;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.service.CarrinhoService;
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
    public CarrinhoResponseDto criarCarrinho() {
        CarrinhoEntity carrinho = carrinhoService.criarCarrinho();
        return carrinhoMapper.toResponseDto(carrinho);
    }

    @GetMapping("/{carrinhoId}")
    public CarrinhoResponseDto buscar(@PathVariable Long carrinhoId) {
        CarrinhoEntity carrinho = carrinhoService.buscarPorId(carrinhoId);
        return carrinhoMapper.toResponseDto(carrinho);
    }

    @PostMapping("/{carrinhoId}/itens")
    public CarrinhoResponseDto adicionarItem(
            @PathVariable Long carrinhoId,
            @Valid @RequestBody CarrinhoAdicionarItemRequestDto dto
    ) {
        CarrinhoEntity carrinho = carrinhoService.adicionarItem(
                carrinhoId,
                dto.getProdutoId(),
                dto.getNomeProduto(),
                dto.getPreco(),
                dto.getQuantidade()
        );
        return carrinhoMapper.toResponseDto(carrinho);
    }

    @DeleteMapping("/{carrinhoId}/itens/{produtoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerItem(@PathVariable Long carrinhoId, @PathVariable Long produtoId) {
        carrinhoService.removerItem(carrinhoId, produtoId);
    }

    @GetMapping("/{carrinhoId}/total")
    public BigDecimal total(@PathVariable Long carrinhoId) {
        return carrinhoService.calcularTotal(carrinhoId);
    }
}
