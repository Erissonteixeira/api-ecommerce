package io.github.Erissonteixeira.api_ecommerce.domain.pedido.controller;

import io.github.Erissonteixeira.api_ecommerce.domain.pedido.dto.PedidoResponseDto;
import io.github.Erissonteixeira.api_ecommerce.domain.pedido.entity.PedidoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.pedido.mapper.PedidoMapper;
import io.github.Erissonteixeira.api_ecommerce.domain.pedido.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/carrinhos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final PedidoMapper pedidoMapper;

    public PedidoController(PedidoService pedidoService, PedidoMapper pedidoMapper) {
        this.pedidoService = pedidoService;
        this.pedidoMapper = pedidoMapper;
    }

    @Operation(
            summary = "Criar pedido a partir do carrinho",
            description = "Fecha o carrinho e gera um pedido com snapshot dos itens"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Carrinho não encontrado"),
            @ApiResponse(responseCode = "400", description = "Carrinho inválido")
    })
    @PostMapping("/{carrinhoId}/pedido")
    public ResponseEntity<PedidoResponseDto> criarPedido(
            @PathVariable Long carrinhoId
    ) {
        PedidoEntity pedido = pedidoService.criarPedidoDoCarrinho(carrinhoId);
        PedidoResponseDto response = pedidoMapper.toResponse(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
