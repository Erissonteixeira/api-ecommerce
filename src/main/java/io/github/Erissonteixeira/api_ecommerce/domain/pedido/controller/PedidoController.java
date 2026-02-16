package io.github.Erissonteixeira.api_ecommerce.domain.pedido.controller;

import io.github.Erissonteixeira.api_ecommerce.domain.pedido.dto.PedidoResponseDto;
import io.github.Erissonteixeira.api_ecommerce.domain.pedido.entity.PedidoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.pedido.mapper.PedidoMapper;
import io.github.Erissonteixeira.api_ecommerce.domain.pedido.service.PedidoService;
import io.github.Erissonteixeira.api_ecommerce.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Pedidos", description = "Endpoints para geração de pedidos")
@RestController
@RequestMapping("/carrinhos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final PedidoMapper pedidoMapper;

    public PedidoController(PedidoService pedidoService, PedidoMapper pedidoMapper) {
        this.pedidoService = pedidoService;
        this.pedidoMapper = pedidoMapper;
    }

    @Operation(summary = "Criar pedido a partir do carrinho", description = "Fecha o carrinho e gera um pedido com snapshot dos itens.")
    @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso")
    @ApiResponse(responseCode = "404", description = "Carrinho não encontrado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(value = """
                            {
                              "timestamp":"2026-01-16T10:00:00-03:00",
                              "status":404,
                              "error":"Not Found",
                              "message":"Carrinho não encontrado",
                              "path":"/carrinhos/999/pedido"
                            }
                            """)))
    @ApiResponse(responseCode = "400", description = "Carrinho inválido",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(value = """
                            {
                              "timestamp":"2026-01-16T10:00:00-03:00",
                              "status":400,
                              "error":"Bad Request",
                              "message":"Não é possível gerar pedido com carrinho vazio",
                              "path":"/carrinhos/1/pedido"
                            }
                            """)))
    @PostMapping("/{carrinhoId}/pedido")
    public ResponseEntity<PedidoResponseDto> criarPedido(@PathVariable Long carrinhoId) {
        PedidoEntity pedido = pedidoService.criarPedidoDoCarrinho(carrinhoId);
        PedidoResponseDto response = pedidoMapper.toResponse(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
