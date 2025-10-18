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
import dev.mello.apiusuario.infrastructure.exception.UnathorizedException;
import dev.mello.apiusuario.infrastructure.repository.EnderecoRepository;
import dev.mello.apiusuario.infrastructure.repository.TelefoneRepository;
import dev.mello.apiusuario.infrastructure.repository.UsuarioRepository;
import dev.mello.apiusuario.infrastructure.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    EnderecoRepository enderecoRepository;

    @Mock
    TelefoneRepository telefoneRepository;

    @Mock
    UsuarioConverter usuarioConverter;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    AuthenticationManager authenticationManager;

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

    List<Usuario> usuariosEntity;
    List<UsuarioResponseDTO> usuariosResponseDTO;

    Long idUsuario;
    Long idEndereco;
    Long idTelefone;

    UsernamePasswordAuthenticationToken authToken;

    String tokenFinal;
    String tokenExperado;
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

        usuariosEntity = List.of(usuarioEntity);
        usuariosResponseDTO = List.of(usuarioResponseDTO);

        idUsuario = 1L;
        idEndereco = 1L;
        idTelefone = 1L;

        email = "teste@email.com";
        tokenFinal = "Bearer tokenFinal";
        tokenExperado = "tokenFinal";
        senhaCriptografada = "senha-criptografada";

        authToken = new UsernamePasswordAuthenticationToken(usuarioRequestDTO.email(), usuarioRequestDTO.senha());
    }

    // Deve Autenticar Usuário
    @Test
    void deveAutenticarUsuarioComSucesso() {
        // GIVEN
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(authToken)).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);
        when(jwtUtil.generateToken(email)).thenReturn(tokenExperado);

        // WHEN
        String resultado = usuarioService.autenticarUsuario(usuarioRequestDTO);

        // THEN
        assertEquals(tokenFinal, resultado);
        verify(authenticationManager).authenticate(authToken);
        verify(jwtUtil).generateToken(email);
        verify(authentication).getName();
    }

    @Test
    void deveGerarExececaoCasoCredenciaisInvalidas() {
        // GIVEN
        when(authenticationManager.authenticate(authToken)).thenThrow(new BadCredentialsException("Credênciais inválidas"));

        // WHEN
        UnathorizedException e = assertThrows(UnathorizedException.class, () -> usuarioService.autenticarUsuario(usuarioRequestDTO));

        // THEN
        assertThat(e.getMessage(), is("Usuário com email ou senha inválidos"));
        assertThat(e.getCause().getClass(), is(BadCredentialsException.class));
        assertThat(e.getCause().getMessage(), is("Credênciais inválidas"));

        verify(authenticationManager).authenticate(authToken);
        verifyNoMoreInteractions(authenticationManager);
    }

    @Test
    void deveGerarExececaoCasoUsuarioNaoEncontrado() {
        // GIVEN
        when(authenticationManager.authenticate(authToken)).thenThrow(new UsernameNotFoundException("Usuário não encontrado"));

        // WHEN
        UnathorizedException e = assertThrows(UnathorizedException.class, () -> usuarioService.autenticarUsuario(usuarioRequestDTO));

        // THEN
        assertThat(e.getMessage(), is("Usuário com email não encontrado"));
        assertThat(e.getCause().getClass(), is(UsernameNotFoundException.class));
        assertThat(e.getCause().getMessage(), is("Usuário não encontrado"));

        verify(authenticationManager).authenticate(authToken);
        verifyNoMoreInteractions(authenticationManager);
    }

    @Test
    void deveGerarExececaoCasoUsuarioNaoTenhaPermissoes() {
        // GIVEN
        when(authenticationManager.authenticate(authToken)).thenThrow(new AuthorizationDeniedException("Usuário sem permissões"));

        // WHEN
        UnathorizedException e = assertThrows(UnathorizedException.class, () -> usuarioService.autenticarUsuario(usuarioRequestDTO));

        // THEN
        assertThat(e.getMessage(), is("Usuário não possuí as permissões necessárias"));
        assertThat(e.getCause().getClass(), is(AuthorizationDeniedException.class));
        assertThat(e.getCause().getMessage(), is("Usuário sem permissões"));

        verify(authenticationManager).authenticate(authToken);
        verifyNoMoreInteractions(authenticationManager);
    }

    // Salvar Usuario
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

    // Gravar Usuario
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
    void naoDeveGravarUsuarioCasoUsuarioRequestDTONull() {
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

    // Atualizar Usuario
    @Test
    void deveAtualizarUsuarioComSucesso() {
        // GIVEN
        when(jwtUtil.extractUsername("tokenFinal")).thenReturn(email);
        when(usuarioRepository.findByEmail("teste@email.com")).thenReturn(Optional.of(usuarioEntity));
        when(passwordEncoder.encode("teste")).thenReturn(senhaCriptografada);
        when(usuarioConverter.updateUsuario(usuarioRequestDTO, usuarioEntity, senhaCriptografada)).thenReturn(usuarioEntity);
        when(usuarioRepository.saveAndFlush(usuarioEntity)).thenReturn(usuarioEntity);
        when(usuarioConverter.toDto(usuarioEntity)).thenReturn(usuarioResponseDTO);

        // WHEN
        UsuarioResponseDTO dto = usuarioService.atualizaDados(tokenFinal, usuarioRequestDTO);

        // THEN
        assertEquals(dto, usuarioResponseDTO);

        verify(jwtUtil).extractUsername("tokenFinal");
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
        BadRequestException e = assertThrows(BadRequestException.class, () -> usuarioService.atualizaDados(tokenFinal, null));

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
        when(jwtUtil.extractUsername("tokenFinal")).thenReturn(email);
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuarioEntity));
        when(passwordEncoder.encode("teste")).thenReturn(senhaCriptografada);
        when(usuarioConverter.updateUsuario(usuarioRequestDTO, usuarioEntity, senhaCriptografada)).thenReturn(usuarioEntity);
        when(usuarioRepository.saveAndFlush(usuarioEntity)).thenThrow(new RuntimeException("Erro ao gravar os dados de usuário"));

        // WHEN
        BadRequestException e = assertThrows(BadRequestException.class, () -> usuarioService.atualizaDados(tokenFinal, usuarioRequestDTO));

        // THEN
        assertThat(e, notNullValue());
        assertThat(e.getMessage(), is("Falha ao gravar dados de usuário"));
        assertThat(e.getCause(), notNullValue());
        assertThat(e.getCause().getClass(), is(RuntimeException.class));
        assertThat(e.getCause().getMessage(), is("Erro ao gravar os dados de usuário"));

        verify(jwtUtil).extractUsername("tokenFinal");
        verify(usuarioRepository).findByEmail(email);
        verify(passwordEncoder).encode("teste");
        verify(usuarioConverter).updateUsuario(usuarioRequestDTO, usuarioEntity, senhaCriptografada);
        verify(usuarioRepository).saveAndFlush(usuarioEntity);
        verifyNoMoreInteractions(usuarioConverter, usuarioRepository, passwordEncoder, jwtUtil);
    }

    // Buscar Usuario
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

    // Remover Usuario
    @Test
    void deveRemoverUsuarioPorEmailComSucesso() {
        // GIVEN
        doNothing().when(usuarioRepository).deleteByEmail(email);

        // WHEN
        usuarioService.deleteByEmail(email);

        // THEN
        verify(usuarioRepository).deleteByEmail(email);
    }

    // Listar Usuarios
    @Test
    void deveListarUsuariosComSucesso() {
        // GIVEN
        when(usuarioRepository.findAll()).thenReturn(usuariosEntity);
        when(usuarioConverter.toDto(usuarioEntity)).thenReturn(usuarioResponseDTO);

        // WHEN
        List<UsuarioResponseDTO> resposta = usuarioService.findAll();

        // THEN
        assertEquals(usuariosResponseDTO, resposta);

        verify(usuarioRepository).findAll();
    }

    @Test
    void deveListarUsuarioComSucessoPeloId() {

        // GIVEN
        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuarioEntity));
        when(usuarioConverter.toDto(usuarioEntity)).thenReturn(usuarioResponseDTO);

        // WHEN
        UsuarioResponseDTO resposta = usuarioService.findById(1L);

        // THEN
        assertEquals(usuarioResponseDTO, resposta);
        verify(usuarioRepository).findById(idUsuario);
    }

    @Test
    void deveGerarExecaoCasoUsuarioNaoEncontradoPorId() {
        // GIVEN
        when(usuarioRepository.findById(idUsuario)).thenThrow(new NotFoundException("Usuário com id: " + idUsuario + " não encontrado!"));

        // WHEN
        NotFoundException e = assertThrows(NotFoundException.class, () -> usuarioService.findById(idUsuario));

        // THEN
        assertThat(e.getMessage(), is("Usuário com id: " + idUsuario + " não encontrado!"));
        verify(usuarioRepository).findById(idUsuario);
        verifyNoInteractions(usuarioConverter);
    }

    @Test
    void deveRemoverUsusarioPorIdComSucesso() {
        // GIVEN
        doNothing().when(usuarioRepository).deleteById(idUsuario);
        when(usuarioRepository.existsById(idUsuario)).thenReturn(true);

        // WHEN
        usuarioService.deleteById(idUsuario);

        // THEN
        verify(usuarioRepository).deleteById(idUsuario);
    }

    @Test
    void deveAtualizarEnderecoDeUsuarioComSucesso() {
        // GIVEN
        when(enderecoRepository.findById(idEndereco)).thenReturn(Optional.of(enderecoEntity));
        when(usuarioConverter.updateEndereco(enderecoRequestDTO, enderecoEntity)).thenReturn(enderecoEntity);
        when(enderecoRepository.saveAndFlush(enderecoEntity)).thenReturn(enderecoEntity);
        when(usuarioConverter.toEnderecoDTO(enderecoEntity)).thenReturn(enderecoResponseDTO);

        // WHEN
        EnderecoResponseDTO dto = usuarioService.atualizaEndereco(idEndereco, enderecoRequestDTO);

        // THEN
        assertEquals(dto, enderecoResponseDTO);
        verify(enderecoRepository).findById(idEndereco);
        verify(usuarioConverter).updateEndereco(enderecoRequestDTO, enderecoEntity);
        verify(enderecoRepository).saveAndFlush(enderecoEntity);
        verify(usuarioConverter).toEnderecoDTO(enderecoEntity);
    }

    @Test
    void deveGerarExecaoCasoEnderecoNaoEncontradoPorIdAoAtualizar() {
        // GIVEN
        when(enderecoRepository.findById(idEndereco)).thenThrow(new NotFoundException("Id do endereço " + idEndereco + " não localizado"));

        // WHEN
        NotFoundException e = assertThrows(NotFoundException.class, () -> usuarioService.buscaEndereceoPorId(idUsuario));

        // THEN
        assertThat(e.getMessage(), is("Id do endereço " + idEndereco + " não localizado"));
        verify(enderecoRepository).findById(idEndereco);
        verifyNoInteractions(usuarioConverter);
    }

    @Test
    void deveAtualizarTelefoneDeUsuarioComSucesso() {
        // GIVEN
        when(telefoneRepository.findById(idTelefone)).thenReturn(Optional.of(telefoneEntity));
        when(usuarioConverter.updateTelefone(telefoneRequestDTO, telefoneEntity)).thenReturn(telefoneEntity);
        when(telefoneRepository.saveAndFlush(telefoneEntity)).thenReturn(telefoneEntity);
        when(usuarioConverter.toTelefoneDTO(telefoneEntity)).thenReturn(telefoneResponseDTO);

        // WHEN
        TelefoneResponseDTO dto = usuarioService.atualizaTelefone(idTelefone, telefoneRequestDTO);

        // THEN
        assertEquals(dto, telefoneResponseDTO);
        verify(telefoneRepository).findById(idTelefone);
        verify(usuarioConverter).updateTelefone(telefoneRequestDTO, telefoneEntity);
        verify(telefoneRepository).saveAndFlush(telefoneEntity);
        verify(usuarioConverter).toTelefoneDTO(telefoneEntity);
    }

    @Test
    void deveGerarExecaoCasoTelefoneNaoEncontradoPorIdAoAtualizar() {
        // GIVEN
        when(telefoneRepository.findById(idTelefone)).thenThrow(new NotFoundException("Id do telefone " + idTelefone + " não localizado"));

        // WHEN
        NotFoundException e = assertThrows(NotFoundException.class, () -> usuarioService.buscaTelefonePorId(idTelefone));

        // THEN
        assertThat(e.getMessage(), is("Id do telefone " + idTelefone + " não localizado"));
        verify(telefoneRepository).findById(idTelefone);
        verifyNoInteractions(usuarioConverter);
    }

    @Test
    void deveAdicionarUmEnderecoComSucesso() {
        // GIVEN
        when(jwtUtil.extractUsername(tokenExperado)).thenReturn(email);
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuarioEntity));
        when(enderecoRepository.saveAndFlush(enderecoEntity)).thenReturn(enderecoEntity);
        when(usuarioConverter.toEnderecoDTO(enderecoEntity)).thenReturn(enderecoResponseDTO);
        when(usuarioConverter.toEndereco(enderecoRequestDTO, idUsuario)).thenReturn(enderecoEntity);

        // WHEN
        EnderecoResponseDTO dto = usuarioService.adicionaEndereco(tokenFinal, enderecoRequestDTO);

        // THEN
        assertEquals(dto, enderecoResponseDTO);
        verify(jwtUtil).extractUsername(tokenExperado);
        verify(usuarioRepository).findByEmail(email);
        verify(enderecoRepository).saveAndFlush(enderecoEntity);
        verify(usuarioConverter).toEnderecoDTO(enderecoEntity);
        verifyNoMoreInteractions(usuarioRepository, usuarioConverter, jwtUtil);
    }

    @Test
    void deveAdicionarUmTelefoneComSucesso() {
        // GIVEN
        when(jwtUtil.extractUsername(tokenExperado)).thenReturn(email);
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuarioEntity));
        when(telefoneRepository.saveAndFlush(telefoneEntity)).thenReturn(telefoneEntity);
        when(usuarioConverter.toTelefoneDTO(telefoneEntity)).thenReturn(telefoneResponseDTO);
        when(usuarioConverter.toTelefone(telefoneRequestDTO, idUsuario)).thenReturn(telefoneEntity);

        // WHEN
        TelefoneResponseDTO dto = usuarioService.adicionaTelefone(tokenFinal, telefoneRequestDTO);

        // THEN
        assertEquals(dto, telefoneResponseDTO);
        verify(jwtUtil).extractUsername(tokenExperado);
        verify(usuarioRepository).findByEmail(email);
        verify(telefoneRepository).saveAndFlush(telefoneEntity);
        verify(usuarioConverter).toTelefoneDTO(telefoneEntity);
        verifyNoMoreInteractions(usuarioRepository, usuarioConverter, jwtUtil);
    }
}
