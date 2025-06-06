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
        String cepFormatado = cep.replace(" ","").replace("-","");

        if (!cepFormatado.matches("\\d+") || !Objects.equals(cepFormatado.length(), 8)) {
            throw new BadRequestException("O cep contém caracteres inválidos, favor verificar!");
        }

        return cepFormatado;
    }
}
