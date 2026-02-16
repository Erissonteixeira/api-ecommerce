package io.github.Erissonteixeira.api_ecommerce.domain.usuario.controller;

import io.github.Erissonteixeira.api_ecommerce.domain.usuario.dto.UsuarioRequestDto;
import io.github.Erissonteixeira.api_ecommerce.domain.usuario.dto.UsuarioResponseDto;
import io.github.Erissonteixeira.api_ecommerce.domain.usuario.service.UsuarioService;
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

@Tag(name = "Usuários", description = "Endpoints para gestão de usuários")
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Criar usuário", description = "Cria um novo usuário na plataforma.")
    @ApiResponse(
            responseCode = "201",
            description = "Usuário criado com sucesso",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UsuarioResponseDto.class)
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
                              "path":"/usuarios",
                              "fieldErrors":[{"field":"email","message":"formato inválido"}]
                            }
                            """)
            )
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponseDto criar(@Valid @RequestBody UsuarioRequestDto dto) {
        return usuarioService.criar(dto);
    }

    @Operation(summary = "Buscar usuário por ID")
    @ApiResponse(
            responseCode = "200",
            description = "Usuário encontrado",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UsuarioResponseDto.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Usuário não encontrado",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @GetMapping("/{id}")
    public UsuarioResponseDto buscarPorId(
            @Parameter(description = "ID do usuário", example = "1")
            @PathVariable Long id
    ) {
        return usuarioService.buscarPorId(id);
    }

    @Operation(summary = "Listar usuários")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public List<UsuarioResponseDto> listar() {
        return usuarioService.listar();
    }

    @Operation(summary = "Atualizar usuário")
    @ApiResponse(
            responseCode = "200",
            description = "Usuário atualizado com sucesso",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UsuarioResponseDto.class)
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
            description = "Usuário não encontrado",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @PutMapping("/{id}")
    public UsuarioResponseDto atualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioRequestDto dto
    ) {
        return usuarioService.atualizar(id, dto);
    }

    @Operation(summary = "Remover usuário")
    @ApiResponse(responseCode = "204", description = "Usuário removido com sucesso")
    @ApiResponse(
            responseCode = "404",
            description = "Usuário não encontrado",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        usuarioService.remover(id);
    }
}
