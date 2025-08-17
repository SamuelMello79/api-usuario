package dev.mello.apiusuario.bussiness.dto.request;

import java.util.List;

public record UsuarioRequestDTO(
        String nome,
        String email,
        String senha,
        List<EnderecoRequestDTO> enderecos,
        List<TelefoneRequestDTO> telefones
) {
}
