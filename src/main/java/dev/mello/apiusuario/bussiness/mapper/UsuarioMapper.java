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
                .nome(usuarioDTO.getNome())
                .email(usuarioDTO.getEmail())
                .senha(usuarioDTO.getSenha())
                .enderecos(toEnderecoList(usuarioDTO.getEnderecos()))
                .telefones(toTelefoneList(usuarioDTO.getTelefones()))
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
                                .numero(enderecoDTO.getNumero())
                                .rua(enderecoDTO.getRua())
                                .cidade(enderecoDTO.getCidade())
                                .estado(enderecoDTO.getEstado())
                                .cep(enderecoDTO.getCep())
                                .build()).toList();
    }

    private List<Telefone> toTelefoneList(List<TelefoneDTO> telefonesDTO) {
        return telefonesDTO.stream()
                .map(telefone ->
                        Telefone.builder()
                                .numero(telefone.getNumero())
                                .ddd(telefone.getDdd())
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

    public Usuario updateUsuario(UsuarioDTO usuarioDTO, Usuario usuario) {
        return Usuario.builder()
                .id(usuario.getId())
                .nome(usuarioDTO.getNome() != null ? usuarioDTO.getNome() : usuario.getNome())
                .email(usuarioDTO.getEmail() != null ? usuarioDTO.getEmail() : usuario.getEmail())
                .senha(usuarioDTO.getSenha() != null ? usuarioDTO.getSenha() : usuario.getSenha())
                .enderecos(usuario.getEnderecos())
                .telefones(usuario.getTelefones())
                .build();
    }

}
