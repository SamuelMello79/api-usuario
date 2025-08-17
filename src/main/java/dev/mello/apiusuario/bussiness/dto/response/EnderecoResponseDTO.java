package dev.mello.apiusuario.bussiness.dto.response;

import lombok.Builder;

@Builder
public record EnderecoResponseDTO(
        Long id,
        String rua,
        Long numero,
        String complemento,
        String cidade,
        String estado,
        String cep
) {
}