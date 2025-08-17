package dev.mello.apiusuario.api.mapper;

import dev.mello.apiusuario.api.requests.EnderecoRequestDTOFixture;
import dev.mello.apiusuario.api.requests.TelefoneRequestDTOFixture;
import dev.mello.apiusuario.api.requests.UsuarioRequestDTOFixture;
import dev.mello.apiusuario.bussiness.dto.request.EnderecoRequestDTO;
import dev.mello.apiusuario.bussiness.dto.request.TelefoneRequestDTO;
import dev.mello.apiusuario.bussiness.dto.request.UsuarioRequestDTO;
import dev.mello.apiusuario.bussiness.mapper.UsuarioConverter;
import dev.mello.apiusuario.infrastructure.entity.Endereco;
import dev.mello.apiusuario.infrastructure.entity.Telefone;
import dev.mello.apiusuario.infrastructure.entity.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UsuarioConverterTest {

    @InjectMocks
    UsuarioConverter usuarioConverter; // Injeta a classe a ser testada

    Usuario usuarioEntity;
    Endereco enderecoEntity;
    Telefone telefoneEntity;

    UsuarioRequestDTO usuarioRequestDTO;
    EnderecoRequestDTO enderecoRequestDTO;
    TelefoneRequestDTO telefoneRequestDTO;

    @BeforeEach
    public void setup() {
        // Criando o mock
        enderecoEntity = Endereco.builder()
                .rua("Rua 1")
                .numero(1200L)
                .complemento("Marmoraria")
                .cidade("Itapetininga")
                .cep("879456455")
                .estado("São Paulo")
                .build();

        telefoneEntity = Telefone.builder()
                .ddd("15")
                .numero("996877889")
                .build();

        usuarioEntity = Usuario.builder()
                .nome("Usuario")
                .email("teste@email.com")
                .senha("teste")
                .enderecos(List.of(enderecoEntity))
                .telefones(List.of(telefoneEntity))
                .build();

        enderecoRequestDTO = EnderecoRequestDTOFixture.build("Rua 1", 1200L, "Marmoraria", "Itapetininga", "São Paulo", "879456455");
        telefoneRequestDTO = TelefoneRequestDTOFixture.build("996877889", "15");
        usuarioRequestDTO = UsuarioRequestDTOFixture.build("Usuario", "teste@email.com", "teste", List.of(enderecoRequestDTO), List.of(telefoneRequestDTO));

    }

    @Test
    void deveConverterParaUsuarioEntity() {
        Usuario entity = usuarioConverter.toEntity(usuarioRequestDTO);
        assertEquals(usuarioEntity, entity);
    }

}
