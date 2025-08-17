package dev.mello.apiusuario.api.mapper;

import dev.mello.apiusuario.api.requests.EnderecoRequestDTOFixture;
import dev.mello.apiusuario.api.requests.TelefoneRequestDTOFixture;
import dev.mello.apiusuario.api.requests.UsuarioRequestDTOFixture;
import dev.mello.apiusuario.bussiness.dto.request.EnderecoRequestDTO;
import dev.mello.apiusuario.bussiness.dto.request.TelefoneRequestDTO;
import dev.mello.apiusuario.bussiness.dto.request.UsuarioRequestDTO;
import dev.mello.apiusuario.bussiness.mapper.UsuarioUpdateMapper;
import dev.mello.apiusuario.infrastructure.entity.Endereco;
import dev.mello.apiusuario.infrastructure.entity.Telefone;
import dev.mello.apiusuario.infrastructure.entity.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UsuarioUpdateMapperTest {

    UsuarioUpdateMapper usuarioUpdateMapper;

    Usuario usuarioEntity;
    Usuario usuarioEntityExpected;

    Endereco enderecoEntity;
    Telefone telefoneEntity;

    UsuarioRequestDTO usuarioRequestDTO;
    EnderecoRequestDTO enderecoRequestDTO;
    TelefoneRequestDTO telefoneRequestDTO;

    @BeforeEach
    public void setup() {
        usuarioUpdateMapper = Mappers.getMapper(UsuarioUpdateMapper.class);
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

        usuarioEntityExpected = Usuario.builder()
                .id(1L)
                .nome("Usuario Teste")
                .email("teste@email.com")
                .senha("teste")
                .enderecos(List.of(enderecoEntity))
                .telefones(List.of(telefoneEntity))
                .build();

        enderecoRequestDTO = EnderecoRequestDTOFixture.build("Rua 1", 1200L, "Marmoraria", "Itapetininga", "São Paulo", "879456455");
        telefoneRequestDTO = TelefoneRequestDTOFixture.build("996877889", "15");
        usuarioRequestDTO = UsuarioRequestDTOFixture.build("Usuario Teste", null, "teste", List.of(enderecoRequestDTO), List.of(telefoneRequestDTO));

    }

    @Test
    void deveConverterParaUsuarioRequestDTO() {
        Usuario entity = usuarioUpdateMapper.updateUsuarioDromDTO(usuarioRequestDTO, usuarioEntity);
        assertEquals(usuarioEntityExpected, entity);
    }

}
