package dev.mello.apiusuario.bussiness;

import dev.mello.apiusuario.api.responses.ViaCepResponseDTOFixture;
import dev.mello.apiusuario.infrastructure.client.ViaCepClient;
import dev.mello.apiusuario.infrastructure.client.ViaCepDTO;
import dev.mello.apiusuario.infrastructure.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ViaCepServiceTest {
    @InjectMocks
    ViaCepService viaCepService;

    @Mock
    ViaCepClient client;

    ViaCepDTO viaCepDTO;

    String cepValido;
    String cepFormatado;

    @BeforeEach
    public void setup() {
        viaCepDTO = ViaCepResponseDTOFixture.build("99999-999", "Prala da sé", "Lado ímpar", "", "Sé", "São Paulo", "SP", "São Paulo", "Sudeste", "3550308", "1004", "11", "7107");

        cepValido = "99999-999";
        cepFormatado = "99999999";
    }

    @Test
    void deveBuscarOsDadosDeEnderecoComSucesso() {
        // GIVEN
        when(client.buscaDadosEndereco(cepFormatado)).thenReturn(viaCepDTO);

        // WHEN
        ViaCepDTO dto = viaCepService.buscarDadosEnedereco(cepValido);

        // THEN
        assertEquals(dto, viaCepDTO);
        verify(client).buscaDadosEndereco(cepFormatado);
        verifyNoMoreInteractions(client);
    }

    @Test
    void deveGerarExcecaoCasoCepForInvalido() {
        // WHEN
        BadRequestException e = assertThrows(BadRequestException.class, () -> viaCepService.buscarDadosEnedereco("12345-99a "));

        // THEN
        assertThat(e.getMessage(), is("O CEP deve conter apenas números!"));
        verifyNoMoreInteractions(client);
    }

    @Test
    void deveGerarExcecaoCasoCepForNulo() {
        // WHEN
        BadRequestException e = assertThrows(BadRequestException.class, () -> viaCepService.buscarDadosEnedereco(null));

        // THEN
        assertThat(e.getMessage(), is("CEP não pode ser vazio ou nulo!"));
        verifyNoMoreInteractions(client);
    }

    @Test
    void deveGerarExcecaoCasoCepForVazio() {
        // WHEN
        BadRequestException e = assertThrows(BadRequestException.class, () -> viaCepService.buscarDadosEnedereco(""));

        // THEN
        assertThat(e.getMessage(), is("CEP não pode ser vazio ou nulo!"));
        verifyNoMoreInteractions(client);
    }

    @Test
    void deveGerarExcecaoCasoCepForMaiorQue8() {
        // WHEN
        BadRequestException e = assertThrows(BadRequestException.class, () -> viaCepService.buscarDadosEnedereco("123456789"));

        // THEN
        assertThat(e.getMessage(), is("O CEP deve ter exatamente 8 digitos!"));
        verifyNoMoreInteractions(client);
    }

    @Test
    void deveGerarExcecaoCasoCepForMenorQue8() {
        // WHEN
        BadRequestException e = assertThrows(BadRequestException.class, () -> viaCepService.buscarDadosEnedereco("1234567"));

        // THEN
        assertThat(e.getMessage(), is("O CEP deve ter exatamente 8 digitos!"));
        verifyNoMoreInteractions(client);
    }
}
