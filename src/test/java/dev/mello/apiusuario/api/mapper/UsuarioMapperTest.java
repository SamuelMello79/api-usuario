package dev.mello.apiusuario.api.mapper;

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
import dev.mello.apiusuario.bussiness.mapper.UsuarioMapper;
import dev.mello.apiusuario.infrastructure.entity.Endereco;
import dev.mello.apiusuario.infrastructure.entity.Telefone;
import dev.mello.apiusuario.infrastructure.entity.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UsuarioMapperTest {

    UsuarioMapper usuarioMapper;

    Usuario usuarioEntity;
    Endereco enderecoEntity;
    Telefone telefoneEntity;

    UsuarioResponseDTO usuarioResponseDTO;
    EnderecoResponseDTO enderecoResponseDTO;
    TelefoneResponseDTO telefoneResponseDTO;

    @BeforeEach
    public void setup() {
        usuarioMapper = Mappers.getMapper(UsuarioMapper.class);
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

        enderecoResponseDTO = EnderecoResponseDTOFixture.build(1L, "Rua 1", 1200L, "Marmoraria", "Itapetininga", "São Paulo", "879456455");
        telefoneResponseDTO = TelefoneResponseDTOFixture.build(1L, "996877889", "15");
        usuarioResponseDTO = UsuarioResponseDTOFixture.build(1L, "Usuario", "teste@email.com", "teste", List.of(enderecoResponseDTO), List.of(telefoneResponseDTO));

    }

    @Test
    void deveConverterParaUsuarioResponseDTO() {
        UsuarioResponseDTO dto = usuarioMapper.paraUsuarioDTO(usuarioEntity);
        assertEquals(usuarioResponseDTO, dto);
    }

}
