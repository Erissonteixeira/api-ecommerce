package io.github.Erissonteixeira.api_ecommerce.domain.auth.service;

import io.github.Erissonteixeira.api_ecommerce.domain.auth.dto.request.LoginRequestDto;
import io.github.Erissonteixeira.api_ecommerce.domain.auth.dto.request.RegisterRequestDto;
import io.github.Erissonteixeira.api_ecommerce.domain.auth.dto.response.MeResponseDto;
import io.github.Erissonteixeira.api_ecommerce.domain.auth.dto.response.TokenResponseDto;
import io.github.Erissonteixeira.api_ecommerce.domain.auth.security.JwtService;
import io.github.Erissonteixeira.api_ecommerce.domain.usuario.dto.UsuarioRequestDto;
import io.github.Erissonteixeira.api_ecommerce.domain.usuario.entity.UsuarioEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.usuario.repository.UsuarioRepository;
import io.github.Erissonteixeira.api_ecommerce.domain.usuario.service.UsuarioService;
import io.github.Erissonteixeira.api_ecommerce.exception.NegocioException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(
            UsuarioRepository usuarioRepository,
            UsuarioService usuarioService,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional
    public TokenResponseDto register(RegisterRequestDto dto) {
        UsuarioRequestDto u = new UsuarioRequestDto();
        u.setNome(dto.getNome());
        u.setEmail(dto.getEmail());
        u.setWhatsapp(dto.getWhatsapp());
        u.setCpf(dto.getCpf());
        u.setSenha(dto.getSenha());

        usuarioService.criar(u);

        String email = dto.getEmail().trim().toLowerCase();

        UsuarioEntity salvo = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new NegocioException("Falha ao criar usuário"));

        String token = jwtService.gerarToken(salvo.getId(), salvo.getEmail());
        return new TokenResponseDto(token);
    }

    @Override
    public TokenResponseDto login(LoginRequestDto dto) {
        String email = dto.getEmail().trim().toLowerCase();

        UsuarioEntity user = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new NegocioException("Credenciais inválidas"));

        if (!passwordEncoder.matches(dto.getSenha(), user.getSenha())) {
            throw new NegocioException("Credenciais inválidas");
        }

        String token = jwtService.gerarToken(user.getId(), user.getEmail());
        return new TokenResponseDto(token);
    }

    @Override
    public MeResponseDto me(String email) {
        String normalized = email == null ? "" : email.trim().toLowerCase();

        UsuarioEntity user = usuarioRepository.findByEmail(normalized)
                .orElseThrow(() -> new NegocioException("Usuário não encontrado"));

        return new MeResponseDto(user.getId(), user.getNome(), user.getEmail());
    }
}