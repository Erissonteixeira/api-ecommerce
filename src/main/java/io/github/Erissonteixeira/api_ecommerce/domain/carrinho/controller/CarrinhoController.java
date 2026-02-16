package io.github.Erissonteixeira.api_ecommerce.domain.carrinho.controller;

import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.dto.CarrinhoAdicionarItemRequestDto;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.dto.CarrinhoResponseDto;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity.CarrinhoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.mapper.CarrinhoMapper;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.service.CarrinhoService;
import io.github.Erissonteixeira.api_ecommerce.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Tag(name = "Carrinhos", description = "Endpoints para carrinho de compras")
@RestController
@RequestMapping("/carrinhos")
public class CarrinhoController {

    private final CarrinhoService carrinhoService;
    private final CarrinhoMapper carrinhoMapper;

    public CarrinhoController(CarrinhoService carrinhoService, CarrinhoMapper carrinhoMapper) {
        this.carrinhoService = carrinhoService;
        this.carrinhoMapper = carrinhoMapper;
    }

    @Operation(summary = "Criar carrinho", description = "Cria um novo carrinho vazio.")
    @ApiResponse(responseCode = "201", description = "Carrinho criado com sucesso")
    @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarrinhoResponseDto criarCarrinho() {
        CarrinhoEntity carrinho = carrinhoService.criarCarrinho();
        return carrinhoMapper.toResponseDto(carrinho);
    }

    @Operation(summary = "Buscar carrinho", description = "Busca o carrinho pelo ID, trazendo itens.")
    @ApiResponse(responseCode = "200", description = "Carrinho encontrado")
    @ApiResponse(responseCode = "404", description = "Carrinho não encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{carrinhoId}")
    public CarrinhoResponseDto buscar(@PathVariable Long carrinhoId) {
        CarrinhoEntity carrinho = carrinhoService.buscarPorId(carrinhoId);
        return carrinhoMapper.toResponseDto(carrinho);
    }

    @Operation(summary = "Adicionar item", description = "Adiciona um produto no carrinho ou incrementa a quantidade se já existir.")
    @ApiResponse(responseCode = "200", description = "Item adicionado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Carrinho não encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
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

    @Operation(summary = "Remover item", description = "Remove 1 unidade do produto. Se quantidade virar 0, remove o item do carrinho.")
    @ApiResponse(responseCode = "204", description = "Item removido com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Carrinho não encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{carrinhoId}/itens/{produtoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerItem(@PathVariable Long carrinhoId, @PathVariable Long produtoId) {
        carrinhoService.removerItem(carrinhoId, produtoId);
    }

    @Operation(summary = "Total do carrinho", description = "Calcula o total do carrinho.")
    @ApiResponse(responseCode = "200", description = "Total calculado com sucesso")
    @ApiResponse(responseCode = "404", description = "Carrinho não encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{carrinhoId}/total")
    public BigDecimal total(@PathVariable Long carrinhoId) {
        return carrinhoService.calcularTotal(carrinhoId);
    }
}
