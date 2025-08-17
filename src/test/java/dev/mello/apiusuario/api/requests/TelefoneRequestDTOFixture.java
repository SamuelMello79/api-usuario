package dev.mello.apiusuario.api.requests;

import dev.mello.apiusuario.bussiness.dto.request.TelefoneRequestDTO;

public record TelefoneRequestDTOFixture(
        String numero,
        String ddd
) {
    public static TelefoneRequestDTO build(String numero, String ddd) {
        return new TelefoneRequestDTO(numero, ddd);
    }
}
