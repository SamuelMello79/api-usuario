package dev.mello.apiusuario.bussiness.dto.request;

public record EnderecoRequestDTO(
        String rua,
        Long numero,
        String complemento,
        String cidade,
        String estado,
        String cep
) {
}
