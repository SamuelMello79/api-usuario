package dev.mello.apiusuario.bussiness.dto;

import lombok.*;

import java.util.List;

@Builder
@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    private String nome;
    private String email;
    private String senha;
    private List<EnderecoDTO> enderecos;
    private List<TelefoneDTO> telefones;
}
