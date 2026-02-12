package io.github.Erissonteixeira.api_ecommerce.domain.usuario.service;

import io.github.Erissonteixeira.api_ecommerce.domain.usuario.dto.UsuarioRequestDto;
import io.github.Erissonteixeira.api_ecommerce.domain.usuario.dto.UsuarioResponseDto;
import io.github.Erissonteixeira.api_ecommerce.domain.usuario.entity.UsuarioEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.usuario.mapper.UsuarioMapper;
import io.github.Erissonteixeira.api_ecommerce.domain.usuario.repository.UsuarioRepository;
import io.github.Erissonteixeira.api_ecommerce.exception.NegocioException;
import io.github.Erissonteixeira.api_ecommerce.exception.RecursoNaoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    @Transactional
    public UsuarioResponseDto criar(UsuarioRequestDto dto) {
        UsuarioEntity entity = usuarioMapper.toEntity(dto);

        normalizar(entity);
        validarUnicidade(entity, null);

        UsuarioEntity salvo = usuarioRepository.save(entity);
        return usuarioMapper.toResponse(salvo);
    }

    @Override
    public UsuarioResponseDto buscarPorId(Long id) {
        UsuarioEntity entity = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));
        return usuarioMapper.toResponse(entity);
    }

    @Override
    public List<UsuarioResponseDto> listar() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public UsuarioResponseDto atualizar(Long id, UsuarioRequestDto dto) {
        UsuarioEntity existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        existente.setNome(dto.getNome());
        existente.setEmail(dto.getEmail());
        existente.setWhatsapp(dto.getWhatsapp());
        existente.setCpf(dto.getCpf());
        existente.setSenha(dto.getSenha());

        normalizar(existente);
        validarUnicidade(existente, id);

        UsuarioEntity salvo = usuarioRepository.save(existente);
        return usuarioMapper.toResponse(salvo);
    }

    @Override
    @Transactional
    public void remover(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Usuário não encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    private void normalizar(UsuarioEntity u) {
        if (u.getNome() != null) u.setNome(u.getNome().trim());
        if (u.getEmail() != null) u.setEmail(u.getEmail().trim().toLowerCase());

        if (u.getCpf() != null) {
            String cpfNumeros = u.getCpf().replaceAll("\\D", "");
            u.setCpf(cpfNumeros);
        }

        if (u.getWhatsapp() != null) {
            u.setWhatsapp(u.getWhatsapp().trim());
        }
    }

    private void validarUnicidade(UsuarioEntity u, Long idAtualizacao) {
        String email = u.getEmail();
        String cpf = u.getCpf();

        if (email == null || email.isBlank()) throw new NegocioException("E-mail é obrigatório");
        if (cpf == null || cpf.isBlank()) throw new NegocioException("CPF é obrigatório");

        boolean emailJaExiste = usuarioRepository.existsByEmail(email);
        boolean cpfJaExiste = usuarioRepository.existsByCpf(cpf);

        if (idAtualizacao == null) {
            if (emailJaExiste) throw new NegocioException("E-mail já cadastrado");
            if (cpfJaExiste) throw new NegocioException("CPF já cadastrado");
            return;
        }

        usuarioRepository.findById(idAtualizacao).ifPresent(atual -> {
            if (!email.equals(atual.getEmail()) && emailJaExiste) {
                throw new NegocioException("E-mail já cadastrado");
            }
            if (!cpf.equals(atual.getCpf()) && cpfJaExiste) {
                throw new NegocioException("CPF já cadastrado");
            }
        });
    }
}
