package io.github.Erissonteixeira.api_ecommerce.domain.auth.service;

import io.github.Erissonteixeira.api_ecommerce.domain.auth.dto.request.LoginRequestDto;
import io.github.Erissonteixeira.api_ecommerce.domain.auth.dto.request.RegisterRequestDto;
import io.github.Erissonteixeira.api_ecommerce.domain.auth.dto.response.MeResponseDto;
import io.github.Erissonteixeira.api_ecommerce.domain.auth.dto.response.TokenResponseDto;
import io.github.Erissonteixeira.api_ecommerce.domain.auth.security.JwtService;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity.CarrinhoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.repository.CarrinhoRepository;
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
    private final CarrinhoRepository carrinhoRepository;

    public AuthServiceImpl(
            UsuarioRepository usuarioRepository,
            UsuarioService usuarioService,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            CarrinhoRepository carrinhoRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.carrinhoRepository = carrinhoRepository;
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

        UsuarioEntity salvo = usuarioRepository.findByEmail(dto.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new NegocioException("Falha ao criar usuário"));

        boolean carrinhoJaExiste = carrinhoRepository.findByUsuarioId(salvo.getId()).isPresent();
        if (!carrinhoJaExiste) {
            carrinhoRepository.save(new CarrinhoEntity(salvo));
        }

        String token = jwtService.gerarToken(salvo.getEmail());
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

        String token = jwtService.gerarToken(user.getEmail());
        return new TokenResponseDto(token);
    }

    @Override
    public MeResponseDto me(String email) {

        UsuarioEntity user = usuarioRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new NegocioException("Usuário não encontrado"));

        return new MeResponseDto(user.getId(), user.getNome(), user.getEmail());
    }
}