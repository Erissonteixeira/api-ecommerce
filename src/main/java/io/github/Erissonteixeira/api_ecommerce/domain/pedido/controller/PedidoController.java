package io.github.Erissonteixeira.api_ecommerce.domain.pedido.controller;

import io.github.Erissonteixeira.api_ecommerce.domain.pedido.dto.PedidoResponseDto;
import io.github.Erissonteixeira.api_ecommerce.domain.pedido.entity.PedidoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.pedido.mapper.PedidoMapper;
import io.github.Erissonteixeira.api_ecommerce.domain.pedido.service.PedidoService;
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

    @PostMapping("/{carrinhoId}/pedido")
    public ResponseEntity<PedidoResponseDto> criarPedido(@PathVariable Long carrinhoId) {

        PedidoEntity pedido = pedidoService.criarPedidoDoCarrinho(carrinhoId);
        PedidoResponseDto response = pedidoMapper.toResponseDto(pedido);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
