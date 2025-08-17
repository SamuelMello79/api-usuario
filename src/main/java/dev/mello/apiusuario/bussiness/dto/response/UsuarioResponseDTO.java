package dev.mello.apiusuario.bussiness.dto.response;

import lombok.*;

import java.util.List;

@Builder
public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email,
        String senha,
        List<EnderecoResponseDTO> enderecos,
        List<TelefoneResponseDTO> telefones
) {
}
