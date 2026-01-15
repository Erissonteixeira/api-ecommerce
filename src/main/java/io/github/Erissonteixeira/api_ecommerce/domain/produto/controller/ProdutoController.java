package io.github.Erissonteixeira.api_ecommerce.domain.produto.controller;

import io.github.Erissonteixeira.api_ecommerce.domain.produto.dto.ProdutoRequestDto;
import io.github.Erissonteixeira.api_ecommerce.domain.produto.dto.ProdutoResponseDto;
import io.github.Erissonteixeira.api_ecommerce.domain.produto.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoResponseDto criar(@RequestBody @Valid ProdutoRequestDto dto) {
        return produtoService.criar(dto);
    }

    @GetMapping("/{id}")
    public ProdutoResponseDto buscarPorId(@PathVariable Long id) {
        return produtoService.buscarPorId(id);
    }

    @GetMapping
    public List<ProdutoResponseDto> listar(@RequestParam(required = false, defaultValue = "false") boolean ativos) {
        return ativos ? produtoService.listarAtivos() : produtoService.listarTodos();
    }

    @PutMapping("/{id}")
    public ProdutoResponseDto atualizar(@PathVariable Long id, @RequestBody @Valid ProdutoRequestDto dto) {
        return produtoService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativar(@PathVariable Long id) {
        produtoService.desativar(id);
    }
}
