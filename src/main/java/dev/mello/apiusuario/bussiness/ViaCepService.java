package dev.mello.apiusuario.bussiness;

import dev.mello.apiusuario.infrastructure.client.ViaCepClient;
import dev.mello.apiusuario.infrastructure.client.ViaCepDTO;
import dev.mello.apiusuario.infrastructure.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ViaCepService {
    private final ViaCepClient client;

    public ViaCepDTO buscarDadosEnedereco(String cep) {
        return client.buscaDadosEndereco(processarCep(cep));
    }

    private String processarCep(String cep) {
        if (cep == null || cep.trim().isEmpty()) {
            throw new BadRequestException("CEP não pode ser vazio ou nulo!");
        }

        String cepFormatado = cep.replace(" ", "").replace("-", "");

        if (!cepFormatado.matches("\\d+")) {
            throw new BadRequestException("O CEP deve conter apenas números!");
        }

        if (cepFormatado.length() != 8) {
            throw new BadRequestException("O CEP deve ter exatamente 8 digitos!");
        }

        return cepFormatado;
    }
}
