package dev.mello.apiusuario.bussiness.dto;

import lombok.Builder;

@Builder
public record EnderecoDTO(
        String rua,
        Long numero,
        String complemento,
        String cidade,
        String estado,
        String cep
) {
}
