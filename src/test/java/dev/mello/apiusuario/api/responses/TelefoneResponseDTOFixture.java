package dev.mello.apiusuario.api.responses;

import dev.mello.apiusuario.bussiness.dto.response.TelefoneResponseDTO;

public record TelefoneResponseDTOFixture(
        Long id,
        String numero,
        String ddd
) {
    public static TelefoneResponseDTO build(Long id, String numero, String ddd) {
        return new TelefoneResponseDTO(id, numero, ddd);
    }
}
