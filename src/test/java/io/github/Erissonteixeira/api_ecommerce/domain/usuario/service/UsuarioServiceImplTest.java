package io.github.Erissonteixeira.api_ecommerce.domain.usuario.service;

import io.github.Erissonteixeira.api_ecommerce.domain.usuario.dto.UsuarioRequestDto;
import io.github.Erissonteixeira.api_ecommerce.domain.usuario.dto.UsuarioResponseDto;
import io.github.Erissonteixeira.api_ecommerce.domain.usuario.entity.UsuarioEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.usuario.mapper.UsuarioMapper;
import io.github.Erissonteixeira.api_ecommerce.domain.usuario.repository.UsuarioRepository;
import io.github.Erissonteixeira.api_ecommerce.exception.NegocioException;
import io.github.Erissonteixeira.api_ecommerce.exception.RecursoNaoEncontradoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @Test
    void deveCriarUsuarioETransformarCpfEmSomenteNumeros() {

        UsuarioRequestDto dto = new UsuarioRequestDto();
        dto.setNome("  Erisson  ");
        dto.setEmail("  ERISSON@EMAIL.COM  ");
        dto.setCpf("123.456.789-00");
        dto.setWhatsapp("51 99999-9999");
        dto.setSenha("Senha@123");

        UsuarioEntity entity = new UsuarioEntity();
        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());
        entity.setCpf(dto.getCpf());
        entity.setWhatsapp(dto.getWhatsapp());
        entity.setSenha(dto.getSenha());

        when(usuarioMapper.toEntity(dto)).thenReturn(entity);
        when(usuarioRepository.existsByEmail("erisson@email.com")).thenReturn(false);
        when(usuarioRepository.existsByCpf("12345678900")).thenReturn(false);
        when(passwordEncoder.encode("Senha@123")).thenReturn("HASH");
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        UsuarioResponseDto response = new UsuarioResponseDto();
        response.setId(1L);
        response.setNome("Erisson");
        response.setEmail("erisson@email.com");

        when(usuarioMapper.toResponse(any(UsuarioEntity.class))).thenReturn(response);

        UsuarioResponseDto salvo = usuarioService.criar(dto);

        assertNotNull(salvo);
        assertEquals(1L, salvo.getId());
        assertEquals("Erisson", salvo.getNome());
        assertEquals("erisson@email.com", salvo.getEmail());

        ArgumentCaptor<UsuarioEntity> captor = ArgumentCaptor.forClass(UsuarioEntity.class);
        verify(usuarioRepository).save(captor.capture());

        UsuarioEntity salvoEntity = captor.getValue();

        assertEquals("Erisson", salvoEntity.getNome().trim());
        assertEquals("erisson@email.com", salvoEntity.getEmail());
        assertEquals("12345678900", salvoEntity.getCpf());
        assertEquals("HASH", salvoEntity.getSenha());
    }

    @Test
    void naoDevePermitirEmailDuplicado() {

        UsuarioRequestDto dto = new UsuarioRequestDto();
        dto.setNome("Erisson");
        dto.setEmail("erisson@email.com");
        dto.setCpf("12345678900");
        dto.setWhatsapp("51 99999-9999");
        dto.setSenha("Senha@123");

        UsuarioEntity entity = new UsuarioEntity();
        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());
        entity.setCpf(dto.getCpf());
        entity.setWhatsapp(dto.getWhatsapp());
        entity.setSenha(dto.getSenha());

        when(usuarioMapper.toEntity(dto)).thenReturn(entity);
        when(usuarioRepository.existsByEmail("erisson@email.com")).thenReturn(true);

        assertThrows(NegocioException.class, () -> usuarioService.criar(dto));

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void naoDevePermitirCpfDuplicado() {

        UsuarioRequestDto dto = new UsuarioRequestDto();
        dto.setNome("Erisson");
        dto.setEmail("erisson@email.com");
        dto.setCpf("123.456.789-00");
        dto.setWhatsapp("51 99999-9999");
        dto.setSenha("Senha@123");

        UsuarioEntity entity = new UsuarioEntity();
        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());
        entity.setCpf(dto.getCpf());
        entity.setWhatsapp(dto.getWhatsapp());
        entity.setSenha(dto.getSenha());

        when(usuarioMapper.toEntity(dto)).thenReturn(entity);
        when(usuarioRepository.existsByEmail("erisson@email.com")).thenReturn(false);
        when(usuarioRepository.existsByCpf("12345678900")).thenReturn(true);

        assertThrows(NegocioException.class, () -> usuarioService.criar(dto));

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void deveBuscarPorId() {

        UsuarioEntity entity = new UsuarioEntity();
        entity.setNome("Erisson");
        entity.setEmail("erisson@email.com");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(entity));

        UsuarioResponseDto response = new UsuarioResponseDto();
        response.setId(1L);
        response.setNome("Erisson");
        response.setEmail("erisson@email.com");

        when(usuarioMapper.toResponse(entity)).thenReturn(response);

        UsuarioResponseDto encontrado = usuarioService.buscarPorId(1L);

        assertEquals(1L, encontrado.getId());
        assertEquals("Erisson", encontrado.getNome());
    }

    @Test
    void deveRemoverUsuario() {

        when(usuarioRepository.existsById(1L)).thenReturn(true);

        usuarioService.remover(1L);

        verify(usuarioRepository).deleteById(1L);
    }

    @Test
    void deveLancarErroAoRemoverInexistente() {

        when(usuarioRepository.existsById(1L)).thenReturn(false);

        assertThrows(RecursoNaoEncontradoException.class, () -> usuarioService.remover(1L));

        verify(usuarioRepository, never()).deleteById(anyLong());
    }

    @Test
    void deveListar() {

        UsuarioEntity a = new UsuarioEntity();
        UsuarioEntity b = new UsuarioEntity();

        when(usuarioRepository.findAll()).thenReturn(List.of(a, b));

        UsuarioResponseDto ra = new UsuarioResponseDto();
        ra.setId(1L);

        UsuarioResponseDto rb = new UsuarioResponseDto();
        rb.setId(2L);

        when(usuarioMapper.toResponse(a)).thenReturn(ra);
        when(usuarioMapper.toResponse(b)).thenReturn(rb);

        List<UsuarioResponseDto> lista = usuarioService.listar();

        assertEquals(2, lista.size());
        assertEquals(1L, lista.get(0).getId());
        assertEquals(2L, lista.get(1).getId());
    }
}