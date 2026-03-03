package io.github.Erissonteixeira.api_ecommerce.domain.auth.service;

import io.github.Erissonteixeira.api_ecommerce.domain.auth.dto.request.LoginRequestDto;
import io.github.Erissonteixeira.api_ecommerce.domain.auth.dto.request.RegisterRequestDto;
import io.github.Erissonteixeira.api_ecommerce.domain.auth.dto.response.MeResponseDto;
import io.github.Erissonteixeira.api_ecommerce.domain.auth.dto.response.TokenResponseDto;

public interface AuthService {
    TokenResponseDto register(RegisterRequestDto dto);
    TokenResponseDto login(LoginRequestDto dto);
    MeResponseDto me(String email);
}