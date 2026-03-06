package io.github.Erissonteixeira.api_ecommerce.domain.pedido.controller;

import io.github.Erissonteixeira.api_ecommerce.domain.pedido.dto.PedidoResponseDto;
import io.github.Erissonteixeira.api_ecommerce.domain.pedido.entity.PedidoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.pedido.mapper.PedidoMapper;
import io.github.Erissonteixeira.api_ecommerce.domain.pedido.service.PedidoService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final PedidoMapper pedidoMapper;

    public PedidoController(PedidoService pedidoService, PedidoMapper pedidoMapper) {
        this.pedidoService = pedidoService;
        this.pedidoMapper = pedidoMapper;
    }

    @PostMapping("/me")
    public PedidoResponseDto criarMeuPedido(Authentication authentication) {
        PedidoEntity pedido = pedidoService.criarPedidoDoCarrinho(authentication.getName());
        return pedidoMapper.toResponseDto(pedido);
    }

    @GetMapping("/me")
    public List<PedidoResponseDto> listarMeusPedidos(Authentication authentication) {
        List<PedidoEntity> pedidos = pedidoService.listarMeusPedidos(authentication.getName());
        return pedidoMapper.toResponseDtoList(pedidos);
    }

    @GetMapping("/me/{id}")
    public PedidoResponseDto buscarMeuPedidoPorId(
            Authentication authentication,
            @PathVariable Long id
    ) {
        PedidoEntity pedido = pedidoService.buscarMeuPedidoPorId(authentication.getName(), id);
        return pedidoMapper.toResponseDto(pedido);
    }
}