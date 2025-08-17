package dev.mello.apiusuario.api.requests;

import dev.mello.apiusuario.bussiness.dto.request.EnderecoRequestDTO;

public record EnderecoRequestDTOFixture(
        String rua,
        Long numero,
        String complemento,
        String cidade,
        String estado,
        String cep
) {
    public static EnderecoRequestDTO build(String rua, Long numero, String complemento, String cidade, String estado, String cep) {
        return new EnderecoRequestDTO(rua, numero, complemento, cidade, estado, cep);
    }
}
