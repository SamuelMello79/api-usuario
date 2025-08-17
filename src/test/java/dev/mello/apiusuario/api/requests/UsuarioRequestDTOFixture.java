package dev.mello.apiusuario.api.requests;

import dev.mello.apiusuario.bussiness.dto.request.EnderecoRequestDTO;
import dev.mello.apiusuario.bussiness.dto.request.TelefoneRequestDTO;
import dev.mello.apiusuario.bussiness.dto.request.UsuarioRequestDTO;

import java.util.List;

public record UsuarioRequestDTOFixture(
        String nome,
        String email,
        String senha,
        List<EnderecoRequestDTOFixture> enderecos,
        List<TelefoneRequestDTOFixture> telefones
) {
    public static UsuarioRequestDTO build(String nome, String email, String senha, List<EnderecoRequestDTO> enderecoRequestDTO, List<TelefoneRequestDTO> telefoneRequestDTO) {
        return new UsuarioRequestDTO(nome, email, senha, enderecoRequestDTO, telefoneRequestDTO);
    }
}
