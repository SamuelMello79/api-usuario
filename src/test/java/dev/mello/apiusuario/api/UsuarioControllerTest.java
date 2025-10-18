package dev.mello.apiusuario.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mello.apiusuario.api.requests.EnderecoRequestDTOFixture;
import dev.mello.apiusuario.api.requests.TelefoneRequestDTOFixture;
import dev.mello.apiusuario.api.requests.UsuarioRequestDTOFixture;
import dev.mello.apiusuario.api.responses.EnderecoResponseDTOFixture;
import dev.mello.apiusuario.api.responses.TelefoneResponseDTOFixture;
import dev.mello.apiusuario.api.responses.UsuarioResponseDTOFixture;
import dev.mello.apiusuario.bussiness.UsuarioService;
import dev.mello.apiusuario.bussiness.dto.request.EnderecoRequestDTO;
import dev.mello.apiusuario.bussiness.dto.request.TelefoneRequestDTO;
import dev.mello.apiusuario.bussiness.dto.request.UsuarioRequestDTO;
import dev.mello.apiusuario.bussiness.dto.response.EnderecoResponseDTO;
import dev.mello.apiusuario.bussiness.dto.response.TelefoneResponseDTO;
import dev.mello.apiusuario.bussiness.dto.response.UsuarioResponseDTO;
import dev.mello.apiusuario.controller.UsuarioController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UsuarioControllerTest {

    @InjectMocks
    UsuarioController usuarioController;

    @Mock
    UsuarioService usuarioService;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String url;


    private EnderecoRequestDTO enderecoRequestDTO;
    private TelefoneRequestDTO telefoneRequestDTO;
    private UsuarioRequestDTO usuarioRequestDTO;

    private EnderecoResponseDTO enderecoResponseDTO;
    private TelefoneResponseDTO telefoneResponseDTO;
    private UsuarioResponseDTO usuarioResponseDTO;

    private List<UsuarioResponseDTO> usuariosResponseDTO;

    private String json;
    private String tokenValido;
    private String email;

    @BeforeEach
    void setup() throws JsonProcessingException {
        enderecoRequestDTO = EnderecoRequestDTOFixture.build("Rua 1", 1200L, "Marmoraria", "Itapetininga", "São Paulo", "879456455");
        telefoneRequestDTO = TelefoneRequestDTOFixture.build("996877889", "15");
        usuarioRequestDTO = UsuarioRequestDTOFixture.build("Usuario", "teste@email.com", "teste", List.of(enderecoRequestDTO), List.of(telefoneRequestDTO));

        enderecoResponseDTO = EnderecoResponseDTOFixture.build(1L, "Rua 1", 1200L, "Marmoraria", "Itapetininga", "São Paulo", "879456455");
        telefoneResponseDTO = TelefoneResponseDTOFixture.build(1L, "996877889", "15");
        usuarioResponseDTO = UsuarioResponseDTOFixture.build(1L, "Usuario", "teste@email.com", "teste", List.of(enderecoResponseDTO), List.of(telefoneResponseDTO));

        usuariosResponseDTO = List.of(usuarioResponseDTO);

        email = "teste@email.com";
        tokenValido = "Bearer tokenValido";

        mockMvc = MockMvcBuilders.standaloneSetup(usuarioController).alwaysDo(print()).build(); // vai exibir os logs
        url = "/usuario"; // url padrão
        json = objectMapper.writeValueAsString(usuarioRequestDTO); // converter o dto em um json
    }

    @Test
    void deveAutenticarUsuarioComSucesso() throws Exception {
        when(usuarioService.autenticarUsuario(usuarioRequestDTO)).thenReturn(tokenValido);

        mockMvc.perform(post(url + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(tokenValido)
                );

        verify(usuarioService).autenticarUsuario(usuarioRequestDTO);
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    void deveGravarDadosDeUsuarioComSucesso() throws Exception {
        when(usuarioService.salvarUsuario(usuarioRequestDTO)).thenReturn(usuarioResponseDTO);

        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isCreated());

        verify(usuarioService).salvarUsuario(usuarioRequestDTO);
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    void naoDeveGravarDadosDeUsuarioCasoJsonNulo() throws Exception {
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());

        verifyNoInteractions(usuarioService);
    }

    @Test
    void deveAtualizarDadosDeUsuarioComSucesso() throws Exception {
        when(usuarioService.atualizaDados(tokenValido, usuarioRequestDTO)).thenReturn(usuarioResponseDTO);


        mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", tokenValido)
                .content(json)
        ).andExpect(status().isOk());

        verify(usuarioService).atualizaDados(tokenValido, usuarioRequestDTO);
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    void naoDeveAtualizarDadosDeUsuarioCasoJsonNulo() throws Exception {
        mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", tokenValido)
        ).andExpect(status().isBadRequest());

        verifyNoInteractions(usuarioService);
    }

    @Test
    void deveBuscarDadosDeUsuarioComSucessoPorEmail() throws Exception {
        when(usuarioService.findByEmail(email)).thenReturn(usuarioResponseDTO);

        mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("email", email)
                .header("Authorization", tokenValido)
        ).andExpect(status().isOk());

        verify(usuarioService).findByEmail(email);
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    void naoDeveAtualizarDadosDeUsuarioCasoParamNulo() throws Exception {
        mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", tokenValido)
        ).andExpect(status().isBadRequest());

        verifyNoInteractions(usuarioService);
    }

    @Test
    void deveRemoverUsuarioPorEmailComSucesso() throws Exception {
        doNothing().when(usuarioService).deleteByEmail(email);

        mockMvc.perform(delete(url + "/email/" + email)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent());

        verify(usuarioService).deleteByEmail(email);
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    void deveListarUsuariosComSucesso() throws Exception {
        when(usuarioService.findAll()).thenReturn(usuariosResponseDTO);

        mockMvc.perform(get(url + "/listar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokenValido)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(usuariosResponseDTO)));

        verify(usuarioService).findAll();
        verifyNoMoreInteractions(usuarioService);
    }
}
