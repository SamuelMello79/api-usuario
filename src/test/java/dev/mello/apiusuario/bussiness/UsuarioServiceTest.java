package dev.mello.apiusuario.bussiness;

import dev.mello.apiusuario.api.requests.EnderecoRequestDTOFixture;
import dev.mello.apiusuario.api.requests.TelefoneRequestDTOFixture;
import dev.mello.apiusuario.api.requests.UsuarioRequestDTOFixture;
import dev.mello.apiusuario.api.responses.EnderecoResponseDTOFixture;
import dev.mello.apiusuario.api.responses.TelefoneResponseDTOFixture;
import dev.mello.apiusuario.api.responses.UsuarioResponseDTOFixture;
import dev.mello.apiusuario.bussiness.dto.request.EnderecoRequestDTO;
import dev.mello.apiusuario.bussiness.dto.request.TelefoneRequestDTO;
import dev.mello.apiusuario.bussiness.dto.request.UsuarioRequestDTO;
import dev.mello.apiusuario.bussiness.dto.response.EnderecoResponseDTO;
import dev.mello.apiusuario.bussiness.dto.response.TelefoneResponseDTO;
import dev.mello.apiusuario.bussiness.dto.response.UsuarioResponseDTO;
import dev.mello.apiusuario.bussiness.mapper.UsuarioConverter;
import dev.mello.apiusuario.infrastructure.entity.Endereco;
import dev.mello.apiusuario.infrastructure.entity.Telefone;
import dev.mello.apiusuario.infrastructure.entity.Usuario;
import dev.mello.apiusuario.infrastructure.exception.BadRequestException;
import dev.mello.apiusuario.infrastructure.exception.NotFoundException;
import dev.mello.apiusuario.infrastructure.repository.UsuarioRepository;
import dev.mello.apiusuario.infrastructure.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // habilita o mockito no Junit5
public class UsuarioServiceTest {

    // cria uma instancia de UsuarioService e injeta os mocks definidos com @Mock nela
    @InjectMocks
    UsuarioService usuarioService;

    // cria mocks para UsuarioRepository, UsuarioConverter, PasswordEncoder, JwtUtil
    @Mock
    UsuarioRepository usuarioRepository;

    @Mock
    UsuarioConverter usuarioConverter;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JwtUtil jwtUtil;

    Usuario usuarioEntity;
    Endereco enderecoEntity;
    Telefone telefoneEntity;

    UsuarioRequestDTO usuarioRequestDTO;
    EnderecoRequestDTO enderecoRequestDTO;
    TelefoneRequestDTO telefoneRequestDTO;

    UsuarioResponseDTO usuarioResponseDTO;
    EnderecoResponseDTO enderecoResponseDTO;
    TelefoneResponseDTO telefoneResponseDTO;

    String tokenValido;
    String email;
    String senhaCriptografada;

    // o metodo é executado antes de cada teste
    // ele cria intancias das entities e dtos com dados ficticios
    // também define algumas variaveis em comum
    @BeforeEach
    public void setup() {
        // Criando o mock
        enderecoEntity = Endereco.builder()
                .id(1L)
                .rua("Rua 1")
                .numero(1200L)
                .complemento("Marmoraria")
                .cidade("Itapetininga")
                .cep("879456455")
                .estado("São Paulo")
                .build();

        telefoneEntity = Telefone.builder()
                .id(1L)
                .ddd("15")
                .numero("996877889")
                .build();

        usuarioEntity = Usuario.builder()
                .id(1L)
                .nome("Usuario")
                .email("teste@email.com")
                .senha("teste")
                .enderecos(List.of(enderecoEntity))
                .telefones(List.of(telefoneEntity))
                .build();

        enderecoRequestDTO = EnderecoRequestDTOFixture.build("Rua 1", 1200L, "Marmoraria", "Itapetininga", "São Paulo", "879456455");
        telefoneRequestDTO = TelefoneRequestDTOFixture.build("996877889", "15");
        usuarioRequestDTO = UsuarioRequestDTOFixture.build("Usuario", "teste@email.com", "teste", List.of(enderecoRequestDTO), List.of(telefoneRequestDTO));

        enderecoResponseDTO = EnderecoResponseDTOFixture.build(1L, "Rua 1", 1200L, "Marmoraria", "Itapetininga", "São Paulo", "879456455");
        telefoneResponseDTO = TelefoneResponseDTOFixture.build(1L, "996877889", "15");
        usuarioResponseDTO = UsuarioResponseDTOFixture.build(1L, "Usuario", "teste@email.com", "teste", List.of(enderecoResponseDTO), List.of(telefoneResponseDTO));

        email = "teste@email.com";
        tokenValido = "Bearer tokenValido";
        senhaCriptografada = "senha-criptografada";
    }

    @Test
    void deveSalvarUsuarioComSucesso() {
        // GIVEN
        // configura usuarioRepository para quando chamar saveAndFlush retornar UsuarioEntity
        when(usuarioRepository.saveAndFlush(usuarioEntity)).thenReturn(usuarioEntity);

        // WHEN
        // Chama o metodo salvaUsuario da UsuarioService
        Usuario entity = usuarioService.salvaUsuario(usuarioEntity);

        // THEN
        // verifica se o retorno do metodo é igual o esperado
        assertEquals(entity, usuarioEntity);

        // verifica se o metodo saveAndFlush foi chamado apenas uma vez
        // verify(usuarioRepository, times(2)).saveAndFlush(usuarioEntity); - o times permite que eu veja quantas vezes foi chamado esse metodo no repository
        verify(usuarioRepository).saveAndFlush(usuarioEntity);
        verifyNoMoreInteractions(usuarioRepository); // verifica se não há mais interações
    }

    @Test
    void deveGravarUsuarioComSucesso() {
        // GIVEN
        when(usuarioRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode("teste")).thenReturn("senha-cripografada");
        when(usuarioConverter.toEntity(usuarioRequestDTO)).thenReturn(usuarioEntity);
        when(usuarioRepository.saveAndFlush(usuarioEntity)).thenReturn(usuarioEntity);
        when(usuarioConverter.toDto(usuarioEntity)).thenReturn(usuarioResponseDTO);

        // WHEN
        UsuarioResponseDTO dto = usuarioService.salvarUsuario(usuarioRequestDTO);

        // THEN
        assertEquals(dto, usuarioResponseDTO);

        verify(usuarioRepository).existsByEmail("teste@email.com");
        verify(passwordEncoder).encode("teste");
        verify(usuarioConverter).toEntity(usuarioRequestDTO);
        verify(usuarioRepository).saveAndFlush(usuarioEntity);
        verify(usuarioConverter).toDto(usuarioEntity);
        verifyNoMoreInteractions(usuarioRepository, usuarioConverter, passwordEncoder);
    }

    @Test
    void naoDeveSalvarUsuarioCasoUsuarioRequestDTONull() {
        // WHEN
        BadRequestException e = assertThrows(BadRequestException.class, () -> usuarioService.salvarUsuario(null));

        // THEN
        assertThat(e, notNullValue());

        assertThat(e.getMessage(), is("Falha ao salvar dados de usuário"));
        assertThat(e.getCause(), notNullValue());
        assertThat(e.getCause().getMessage(), is("Os dados do usuário são obrigatórios"));

        verifyNoMoreInteractions(usuarioConverter, usuarioRepository, passwordEncoder);
    }

    @Test
    void deveGerarExcecaoCasoOcorraErroAoGravarUsuario() {
        // GIVEN
        when(usuarioRepository.existsByEmail("teste@email.com")).thenReturn(false);
        when(passwordEncoder.encode("teste")).thenReturn("senha-criptografada");
        when(usuarioConverter.toEntity(usuarioRequestDTO)).thenReturn(usuarioEntity);
        when(usuarioRepository.saveAndFlush(usuarioEntity)).thenThrow(new RuntimeException("Falha ao gravar os dados de usuário"));

        // WHEN
        BadRequestException e = assertThrows(BadRequestException.class, () -> usuarioService.salvarUsuario(usuarioRequestDTO));

        // THEN
        assertThat(e, notNullValue());
        assertThat(e.getMessage(), is("Falha ao salvar dados de usuário"));
        assertThat(e.getCause(), notNullValue());
        assertThat(e.getCause().getClass(), is(RuntimeException.class));
        assertThat(e.getCause().getMessage(), is("Falha ao gravar os dados de usuário"));

        verify(usuarioRepository).existsByEmail("teste@email.com");
        verify(passwordEncoder).encode("teste");
        verify(usuarioConverter).toEntity(usuarioRequestDTO);
        verify(usuarioRepository).saveAndFlush(usuarioEntity);
        verifyNoMoreInteractions(usuarioConverter, usuarioRepository, passwordEncoder);
    }

    @Test
    void deveAtualizarUsuarioComSucesso() {
        // GIVEN
        when(jwtUtil.extractUsername("tokenValido")).thenReturn(email);
        when(usuarioRepository.findByEmail("teste@email.com")).thenReturn(Optional.of(usuarioEntity));
        when(passwordEncoder.encode("teste")).thenReturn(senhaCriptografada);
        when(usuarioConverter.updateUsuario(usuarioRequestDTO, usuarioEntity, senhaCriptografada)).thenReturn(usuarioEntity);
        when(usuarioRepository.saveAndFlush(usuarioEntity)).thenReturn(usuarioEntity);
        when(usuarioConverter.toDto(usuarioEntity)).thenReturn(usuarioResponseDTO);

        // WHEN
        UsuarioResponseDTO dto = usuarioService.atualizaDados(tokenValido, usuarioRequestDTO);

        // THEN
        assertEquals(dto, usuarioResponseDTO);

        verify(jwtUtil).extractUsername("tokenValido");
        verify(usuarioRepository).findByEmail(email);
        verify(passwordEncoder).encode("teste");
        verify(usuarioConverter).updateUsuario(usuarioRequestDTO, usuarioEntity, senhaCriptografada);
        verify(usuarioRepository).saveAndFlush(usuarioEntity);
        verify(usuarioConverter).toDto(usuarioEntity);
        verifyNoMoreInteractions(usuarioRepository, usuarioConverter, passwordEncoder, jwtUtil);
    }

    @Test
    void naoDeveAtualizarUsuarioCasoUsuarioRequestDTONull() {
        // WHEN
        BadRequestException e = assertThrows(BadRequestException.class, () -> usuarioService.atualizaDados(tokenValido, null));

        // THEN
        assertThat(e, notNullValue());
        assertThat(e.getMessage(), is("Falha ao gravar dados de usuário"));
        assertThat(e.getCause(), notNullValue());
        assertThat(e.getCause().getMessage(), is("Os dados do usuário são obrigatórios"));

        verifyNoMoreInteractions(usuarioConverter, usuarioRepository, passwordEncoder, jwtUtil);
    }

    @Test
    void deveGerarExcecaoCasoOcorraErroAoAtualizarUsuario() {
        // GIVEN
        when(jwtUtil.extractUsername("tokenValido")).thenReturn(email);
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuarioEntity));
        when(passwordEncoder.encode("teste")).thenReturn(senhaCriptografada);
        when(usuarioConverter.updateUsuario(usuarioRequestDTO, usuarioEntity, senhaCriptografada)).thenReturn(usuarioEntity);
        when(usuarioRepository.saveAndFlush(usuarioEntity)).thenThrow(new RuntimeException("Erro ao gravar os dados de usuário"));

        // WHEN
        BadRequestException e = assertThrows(BadRequestException.class, () -> usuarioService.atualizaDados(tokenValido, usuarioRequestDTO));

        // THEN
        assertThat(e, notNullValue());
        assertThat(e.getMessage(), is("Falha ao gravar dados de usuário"));
        assertThat(e.getCause(), notNullValue());
        assertThat(e.getCause().getClass(), is(RuntimeException.class));
        assertThat(e.getCause().getMessage(), is("Erro ao gravar os dados de usuário"));

        verify(jwtUtil).extractUsername("tokenValido");
        verify(usuarioRepository).findByEmail(email);
        verify(passwordEncoder).encode("teste");
        verify(usuarioConverter).updateUsuario(usuarioRequestDTO, usuarioEntity, senhaCriptografada);
        verify(usuarioRepository).saveAndFlush(usuarioEntity);
        verifyNoMoreInteractions(usuarioConverter, usuarioRepository, passwordEncoder, jwtUtil);
    }

    @Test
    void deveBuscarDadosDeUsuarioPorEmailComSucesso() {
        // GIVEN
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuarioEntity));
        when(usuarioConverter.toDto(usuarioEntity)).thenReturn(usuarioResponseDTO);

        // WHEN
        UsuarioResponseDTO dto = usuarioService.findByEmail(email);

        // THEN
        assertEquals(dto, usuarioResponseDTO);

        verify(usuarioRepository).findByEmail(email);
        verify(usuarioConverter).toDto(usuarioEntity);
        verifyNoMoreInteractions(usuarioRepository, usuarioConverter);
    }

    @Test
    void deveGerarExecaoCasoUsuarioNaoEncontradoPorEmail() {
        // GIVEN
        when(usuarioRepository.findByEmail(email)).thenThrow(new NotFoundException("Usuário com email: " + email + " não encontrado"));

        // WHEN
        NotFoundException e = assertThrows(NotFoundException.class, () -> usuarioService.findByEmail(email));

        // THEN
        assertThat(e.getMessage(), is("Usuário com email: " + email + " não encontrado"));

        verify(usuarioRepository).findByEmail(email);
        verifyNoInteractions(usuarioConverter);
    }

    @Test
    void deveRemoverUsuarioPorEmailComSucesso() {
        // GIVEN
        doNothing().when(usuarioRepository).deleteByEmail(email);

        // WHEN
        usuarioService.deleteByEmail(email);

        // THEN
        verify(usuarioRepository).deleteByEmail(email);
    }
}
