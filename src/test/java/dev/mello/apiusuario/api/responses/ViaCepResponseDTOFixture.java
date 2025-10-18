package dev.mello.apiusuario.api.responses;

import dev.mello.apiusuario.infrastructure.client.ViaCepDTO;

public record ViaCepResponseDTOFixture(
        String cep,
        String logradouro,
        String complemento,
        String unidade,
        String bairro,
        String localidade,
        String uf,
        String estado,
        String regiao,
        String ibge,
        String gia,
        String ddd,
        String siafi
) {
    public static ViaCepDTO build(String cep, String logradouro,
                                  String complemento,
                                  String unidade,
                                  String bairro,
                                  String localidade,
                                  String uf,
                                  String estado,
                                  String regiao,
                                  String ibge,
                                  String gia,
                                  String ddd,
                                  String siafi) {
        return new ViaCepDTO(cep, logradouro, complemento, unidade, bairro, localidade, uf, estado, regiao, ibge, gia, ddd, siafi);
    }
}
