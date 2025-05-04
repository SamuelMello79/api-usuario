package dev.mello.apiusuario.bussiness.mapper;

import dev.mello.apiusuario.bussiness.dto.EnderecoDTO;
import dev.mello.apiusuario.bussiness.dto.TelefoneDTO;
import dev.mello.apiusuario.bussiness.dto.UsuarioDTO;
import dev.mello.apiusuario.infrastructure.entity.Endereco;
import dev.mello.apiusuario.infrastructure.entity.Telefone;
import dev.mello.apiusuario.infrastructure.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsuarioMapper {
    public Usuario toEntity(UsuarioDTO usuarioDTO) {
        return Usuario.builder()
                .nome(usuarioDTO.nome())
                .email(usuarioDTO.email())
                .senha(usuarioDTO.senha())
                .enderecos(toEnderecoList(usuarioDTO.enderecos()))
                .telefones(toTelefoneList(usuarioDTO.telefones()))
                .build();
    }

    public UsuarioDTO toDto(Usuario usuario) {
        return UsuarioDTO.builder()
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .senha(usuario.getSenha())
                .enderecos(toEnderecoDTOList(usuario.getEnderecos()))
                .telefones(toTelefoneDTOList(usuario.getTelefones()))
                .build();
    }

    private List<Endereco> toEnderecoList(List<EnderecoDTO> enderecosDTO) {
        return enderecosDTO.stream()
                .map(enderecoDTO ->
                        Endereco.builder()
                                .numero(enderecoDTO.numero())
                                .rua(enderecoDTO.rua())
                                .cidade(enderecoDTO.cidade())
                                .estado(enderecoDTO.estado())
                                .cep(enderecoDTO.cep())
                                .build()).toList();
    }

    private List<Telefone> toTelefoneList(List<TelefoneDTO> telefonesDTO) {
        return telefonesDTO.stream()
                .map(telefone ->
                        Telefone.builder()
                                .numero(telefone.numero())
                                .ddd(telefone.ddd())
                                .build()).toList();
    }

    private List<EnderecoDTO> toEnderecoDTOList(List<Endereco> enderecos) {
        return enderecos.stream()
                .map(endereco ->
                        EnderecoDTO.builder()
                                .rua(endereco.getRua())
                                .numero(endereco.getNumero())
                                .complemento(endereco.getComplemento())
                                .cep(endereco.getCep())
                                .cidade(endereco.getCidade())
                                .estado(endereco.getEstado())
                                .build()).toList();
    }

    private List<TelefoneDTO> toTelefoneDTOList(List<Telefone> telefones) {
        return telefones.stream()
                .map(telefone ->
                        TelefoneDTO.builder()
                                .numero(telefone.getNumero())
                                .ddd(telefone.getDdd())
                                .build()).toList();
    }

}
