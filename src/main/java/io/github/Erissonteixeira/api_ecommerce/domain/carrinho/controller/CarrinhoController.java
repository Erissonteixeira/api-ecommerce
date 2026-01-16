package io.github.Erissonteixeira.api_ecommerce.domain.carrinho.controller;

import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.mapper.CarrinhoMapper;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.service.CarrinhoService;
import io.github.Erissonteixeira.api_ecommerce.domain.dto.CarrinhoAdicionarItemRequestDto;
import io.github.Erissonteixeira.api_ecommerce.domain.dto.CarrinhoResponseDto;
import io.github.Erissonteixeira.api_ecommerce.exception.OpenApiErrorSchemas;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Tag(name = "Carrinhos", description = "Endpoints para operações do carrinho de compras")
@RestController
@RequestMapping("/carrinhos")
public class CarrinhoController {

    private final CarrinhoService carrinhoService;
    private final CarrinhoMapper carrinhoMapper;

    public CarrinhoController(CarrinhoService carrinhoService, CarrinhoMapper carrinhoMapper) {
        this.carrinhoService = carrinhoService;
        this.carrinhoMapper = carrinhoMapper;
    }

    @Operation(summary = "Criar carrinho", description = "Cria um carrinho vazio e retorna seu ID.")
    @ApiResponse(responseCode = "201", description = "Carrinho criado com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CarrinhoResponseDto.class),
                    examples = @ExampleObject(value = """
                                {
                                  "id": 1,
                                  "criadoEm": "2026-01-16T10:00:00",
                                  "atualizadoEm": null,
                                  "itens": [],
                                  "total": 0
                                }
                            """)
            )
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarrinhoResponseDto criar() {
        return carrinhoMapper.toResponse(carrinhoService.criarCarrinho());
    }

    @Operation(summary = "Buscar carrinho por ID", description = "Retorna o carrinho com seus itens.")
    @ApiResponse(responseCode = "200", description = "Carrinho encontrado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CarrinhoResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Carrinho não encontrado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = OpenApiErrorSchemas.class)))
    @GetMapping("/{carrinhoId}")
    public CarrinhoResponseDto buscarPorId(
            @Parameter(description = "ID do carrinho", example = "1")
            @PathVariable Long carrinhoId
    ) {
        return carrinhoMapper.toResponse(carrinhoService.buscarPorId(carrinhoId));
    }

    @Operation(summary = "Adicionar item ao carrinho", description = "Adiciona um item ao carrinho (ou incrementa se já existir).")
    @ApiResponse(responseCode = "200", description = "Item adicionado com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CarrinhoResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = OpenApiErrorSchemas.class),
                    examples = @ExampleObject(value = """
                                {
                                  "timestamp":"2026-01-16T10:00:00-03:00",
                                  "status":400,
                                  "error":"Bad Request",
                                  "message":"Dados inválidos",
                                  "path":"/carrinhos/1/itens",
                                  "fieldErrors":[{"field":"quantidade","message":"deve ser maior que zero"}]
                                }
                            """)
            )
    )
    @ApiResponse(responseCode = "404", description = "Carrinho não encontrado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = OpenApiErrorSchemas.class)))
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

    @Operation(summary = "Remover item do carrinho", description = "Remove 1 unidade do item. Se chegar em 0, remove o item.")
    @ApiResponse(responseCode = "204", description = "Item removido com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = OpenApiErrorSchemas.class)))
    @ApiResponse(responseCode = "404", description = "Carrinho não encontrado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = OpenApiErrorSchemas.class)))
    @DeleteMapping("/{carrinhoId}/itens/{produtoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerItem(
            @PathVariable Long carrinhoId,
            @PathVariable Long produtoId
    ) {
        carrinhoService.removerItem(carrinhoId, produtoId);
    }

    @Operation(summary = "Calcular total do carrinho", description = "Calcula e retorna o total do carrinho.")
    @ApiResponse(responseCode = "200", description = "Total calculado com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BigDecimal.class),
                    examples = @ExampleObject(value = "100.00")))
    @ApiResponse(responseCode = "404", description = "Carrinho não encontrado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = OpenApiErrorSchemas.class)))
    @GetMapping("/{carrinhoId}/total")
    public BigDecimal calcularTotal(@PathVariable Long carrinhoId) {
        return carrinhoService.calcularTotal(carrinhoId);
    }
}
