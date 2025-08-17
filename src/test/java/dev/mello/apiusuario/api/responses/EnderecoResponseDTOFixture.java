package dev.mello.apiusuario.api.responses;


import dev.mello.apiusuario.bussiness.dto.response.EnderecoResponseDTO;

public record EnderecoResponseDTOFixture(
        Long id,
        String rua,
        Long numero,
        String complemento,
        String cidade,
        String estado,
        String cep
) {
    public static EnderecoResponseDTO build(Long id, String rua, Long numero, String complemento, String cidade, String estado, String cep) {
        return new EnderecoResponseDTO(id, rua, numero, complemento, cidade, estado, cep);
    }
}