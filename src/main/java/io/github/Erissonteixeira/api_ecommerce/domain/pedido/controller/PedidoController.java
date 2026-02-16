package io.github.Erissonteixeira.api_ecommerce.domain.pedido.controller;

import io.github.Erissonteixeira.api_ecommerce.domain.pedido.dto.PedidoResponseDto;
import io.github.Erissonteixeira.api_ecommerce.domain.pedido.entity.PedidoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.pedido.mapper.PedidoMapper;
import io.github.Erissonteixeira.api_ecommerce.domain.pedido.service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carrinhos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final PedidoMapper pedidoMapper;

    public PedidoController(PedidoService pedidoService,
                            PedidoMapper pedidoMapper) {
        this.pedidoService = pedidoService;
        this.pedidoMapper = pedidoMapper;
    }

    @PostMapping("/{carrinhoId}/pedido")
    public ResponseEntity<PedidoResponseDto> criarPedido(
            @PathVariable Long carrinhoId
    ) {
        PedidoEntity pedido = pedidoService.criarPedidoDoCarrinho(carrinhoId);
        PedidoResponseDto response = pedidoMapper.toResponse(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
