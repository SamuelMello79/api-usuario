package dev.mello.apiusuario.bussiness.dto;

import lombok.Builder;

@Builder
public record TelefoneDTO(
        String numero,
        String ddd
) {
}
