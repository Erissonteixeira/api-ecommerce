package io.github.Erissonteixeira.api_ecommerce.domain.pedido.controller;

import io.github.Erissonteixeira.api_ecommerce.domain.pedido.entity.PedidoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.pedido.service.PedidoService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping("/me")
    public PedidoEntity criarMeuPedido(Authentication authentication) {
        return pedidoService.criarPedidoDoCarrinho(authentication.getName());
    }

    @GetMapping("/me")
    public List<PedidoEntity> listarMeusPedidos(Authentication authentication) {
        return pedidoService.listarMeusPedidos(authentication.getName());
    }

    @GetMapping("/me/{id}")
    public PedidoEntity buscarMeuPedidoPorId(
            Authentication authentication,
            @PathVariable Long id
    ) {
        return pedidoService.buscarMeuPedidoPorId(authentication.getName(), id);
    }
}