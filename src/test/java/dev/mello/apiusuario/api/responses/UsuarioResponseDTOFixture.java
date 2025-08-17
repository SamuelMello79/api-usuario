package dev.mello.apiusuario.api.responses;

import dev.mello.apiusuario.bussiness.dto.response.EnderecoResponseDTO;
import dev.mello.apiusuario.bussiness.dto.response.TelefoneResponseDTO;
import dev.mello.apiusuario.bussiness.dto.response.UsuarioResponseDTO;

import java.util.List;

public record UsuarioResponseDTOFixture(
        Long id,
        String nome,
        String email,
        String senha,
        List<EnderecoResponseDTOFixture> enderecos,
        List<TelefoneResponseDTOFixture> telefones
) {
    public static UsuarioResponseDTO build(Long id, String nome, String email, String senha, List<EnderecoResponseDTO> enderecos, List<TelefoneResponseDTO> telefones) {
        return new UsuarioResponseDTO(id, nome, email, senha, enderecos, telefones);
    }
}
