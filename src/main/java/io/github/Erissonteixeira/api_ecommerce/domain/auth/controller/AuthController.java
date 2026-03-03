package io.github.Erissonteixeira.api_ecommerce.domain.auth.controller;

import io.github.Erissonteixeira.api_ecommerce.domain.auth.dto.request.LoginRequestDto;
import io.github.Erissonteixeira.api_ecommerce.domain.auth.dto.request.RegisterRequestDto;
import io.github.Erissonteixeira.api_ecommerce.domain.auth.dto.response.MeResponseDto;
import io.github.Erissonteixeira.api_ecommerce.domain.auth.dto.response.TokenResponseDto;
import io.github.Erissonteixeira.api_ecommerce.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public TokenResponseDto register(@Valid @RequestBody RegisterRequestDto dto) {
        return authService.register(dto);
    }

    @PostMapping("/login")
    public TokenResponseDto login(@Valid @RequestBody LoginRequestDto dto) {
        return authService.login(dto);
    }

    @GetMapping("/me")
    public MeResponseDto me(Authentication authentication) {

        String email = (String) authentication.getPrincipal();
        return authService.me(email);
    }
}