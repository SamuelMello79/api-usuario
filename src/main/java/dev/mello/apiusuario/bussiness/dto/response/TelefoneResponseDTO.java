package dev.mello.apiusuario.bussiness.dto.response;

import lombok.*;

@Builder
public record TelefoneResponseDTO(
        Long id,
        String numero,
        String ddd
) {
}
