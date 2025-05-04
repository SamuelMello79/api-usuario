package dev.mello.apiusuario.bussiness.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record UsuarioDTO(
        String nome,
        String email,
        String senha,
        List<EnderecoDTO> enderecos,
        List<TelefoneDTO> telefones
) {
}
