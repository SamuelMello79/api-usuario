package dev.mello.apiusuario.bussiness.dto;

import lombok.*;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class TelefoneDTO {
    private String numero;
    private String ddd;
}
