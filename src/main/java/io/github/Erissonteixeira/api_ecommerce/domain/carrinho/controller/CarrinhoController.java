package io.github.Erissonteixeira.api_ecommerce.domain.carrinho.controller;

import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.dto.CarrinhoAdicionarItemRequestDto;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.dto.CarrinhoResponseDto;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity.CarrinhoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.mapper.CarrinhoMapper;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.service.CarrinhoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Carrinho", description = "Endpoints do carrinho do usuário logado")
@RestController
@RequestMapping("/carrinho")
public class CarrinhoController {

    private final CarrinhoService carrinhoService;
    private final CarrinhoMapper carrinhoMapper;

    public CarrinhoController(CarrinhoService carrinhoService, CarrinhoMapper carrinhoMapper) {
        this.carrinhoService = carrinhoService;
        this.carrinhoMapper = carrinhoMapper;
    }

    @Operation(summary = "Meu carrinho", description = "Retorna o carrinho do usuário logado (cria se não existir).")
    @ApiResponse(responseCode = "200", description = "Carrinho retornado com sucesso")
    @GetMapping
    public CarrinhoResponseDto meuCarrinho(Authentication authentication) {
        CarrinhoEntity carrinho = carrinhoService.obterOuCriarCarrinho(authentication.getName());
        return carrinhoMapper.toResponseDto(carrinho);
    }

    @Operation(summary = "Adicionar item", description = "Adiciona item no carrinho do usuário logado.")
    @ApiResponse(responseCode = "200", description = "Item adicionado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    @PostMapping("/itens")
    public CarrinhoResponseDto adicionarItem(
            Authentication authentication,
            @Valid @RequestBody CarrinhoAdicionarItemRequestDto dto
    ) {
        CarrinhoEntity carrinho = carrinhoService.adicionarItem(
                authentication.getName(),
                dto.getProdutoId(),
                dto.getQuantidade()
        );
        return carrinhoMapper.toResponseDto(carrinho);
    }

    @Operation(summary = "Remover item", description = "Remove 1 unidade do produto; se zerar, remove o item.")
    @ApiResponse(responseCode = "204", description = "Item removido com sucesso")
    @DeleteMapping("/itens/{produtoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerItem(Authentication authentication, @PathVariable Long produtoId) {
        carrinhoService.removerItem(authentication.getName(), produtoId);
    }

    @Operation(summary = "Limpar carrinho", description = "Remove todos os itens do carrinho do usuário logado.")
    @ApiResponse(responseCode = "204", description = "Carrinho limpo com sucesso")
    @DeleteMapping("/limpar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void limpar(Authentication authentication) {
        carrinhoService.limpar(authentication.getName());
    }
}