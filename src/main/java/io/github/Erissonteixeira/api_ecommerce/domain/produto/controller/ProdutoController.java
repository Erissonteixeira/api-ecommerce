package io.github.Erissonteixeira.api_ecommerce.domain.produto.controller;

import io.github.Erissonteixeira.api_ecommerce.domain.produto.dto.ProdutoRequestDto;
import io.github.Erissonteixeira.api_ecommerce.domain.produto.dto.ProdutoResponseDto;
import io.github.Erissonteixeira.api_ecommerce.domain.produto.service.ProdutoService;
import io.github.Erissonteixeira.api_ecommerce.exception.ErrorResponse;
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

import java.util.List;

@Tag(name = "Produtos", description = "Endpoints para gestão de produtos")
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @Operation(
            summary = "Criar produto",
            description = "Cria um novo produto e retorna o produto criado."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Produto criado com sucesso",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProdutoResponseDto.class),
                    examples = @ExampleObject(value = """
                            {
                              "id": 1,
                              "nome": "Mouse Gamer",
                              "preco": 199.90,
                              "ativo": true,
                              "criadoEm": "2026-01-16T10:00:00",
                              "atualizadoEm": null
                            }
                            """)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(value = """
                            {
                              "timestamp":"2026-01-16T10:00:00-03:00",
                              "status":400,
                              "error":"Bad Request",
                              "message":"Dados inválidos",
                              "path":"/produtos",
                              "fieldErrors":[{"field":"nome","message":"não deve estar em branco"}]
                            }
                            """)
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Erro interno inesperado",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoResponseDto criar(@RequestBody @Valid ProdutoRequestDto dto) {
        return produtoService.criar(dto);
    }

    @Operation(
            summary = "Buscar produto por ID",
            description = "Busca um produto pelo ID."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Produto encontrado",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProdutoResponseDto.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Produto não encontrado",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(value = """
                            {
                              "timestamp":"2026-01-16T10:00:00-03:00",
                              "status":404,
                              "error":"Not Found",
                              "message":"Produto não encontrado",
                              "path":"/produtos/999"
                            }
                            """)
            )
    )
    @GetMapping("/{id}")
    public ProdutoResponseDto buscarPorId(
            @Parameter(description = "ID do produto", example = "1")
            @PathVariable Long id
    ) {
        return produtoService.buscarPorId(id);
    }

    @Operation(
            summary = "Listar produtos",
            description = "Lista todos os produtos. Se a query param ativos=true, lista apenas os produtos ativos."
    )
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public List<ProdutoResponseDto> listarTodos(
            @Parameter(description = "Se true, retorna apenas produtos ativos. Se false ou não informado, retorna todos.", example = "true")
            @RequestParam(name = "ativos", required = false) Boolean ativos
    ) {
        if (Boolean.TRUE.equals(ativos)) {
            return produtoService.listarAtivos();
        }
        return produtoService.listarTodos();
    }

    @Operation(summary = "Listar produtos ativos", description = "Lista apenas produtos com ativo=true.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping("/ativos")
    public List<ProdutoResponseDto> listarAtivos() {
        return produtoService.listarAtivos();
    }

    @Operation(summary = "Atualizar produto", description = "Atualiza nome, preço e ativo de um produto.")
    @ApiResponse(
            responseCode = "200",
            description = "Produto atualizado com sucesso",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProdutoResponseDto.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Produto não encontrado",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @PutMapping("/{id}")
    public ProdutoResponseDto atualizar(
            @PathVariable Long id,
            @RequestBody @Valid ProdutoRequestDto dto
    ) {
        return produtoService.atualizar(id, dto);
    }

    @Operation(summary = "Desativar produto", description = "Marca o produto como ativo=false.")
    @ApiResponse(responseCode = "204", description = "Produto desativado com sucesso")
    @ApiResponse(
            responseCode = "404",
            description = "Produto não encontrado",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativar(@PathVariable Long id) {
        produtoService.desativar(id);
    }
}
