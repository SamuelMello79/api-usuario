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
                .map(this::toEndereco).toList();
    }

    private List<Telefone> toTelefoneList(List<TelefoneDTO> telefonesDTO) {
        return telefonesDTO.stream()
                .map(this::toTelefone).toList();
    }

    public Telefone toTelefone(TelefoneDTO telefoneDTO) {
        return Telefone.builder()
                .numero(telefoneDTO.getNumero())
                .ddd(telefoneDTO.getDdd())
                .build();
    }

    public Endereco toEndereco(EnderecoDTO enderecoDTO) {
        return Endereco.builder()
                .rua(enderecoDTO.getRua())
                .numero(enderecoDTO.getNumero())
                .complemento(enderecoDTO.getComplemento())
                .cep(enderecoDTO.getCep())
                .cidade(enderecoDTO.getCidade())
                .estado(enderecoDTO.getEstado())
                .build();
    }

    private List<EnderecoDTO> toEnderecoDTOList(List<Endereco> enderecos) {
        return enderecos.stream()
                .map(this::toEnderecoDTO).toList();
    }

    private List<TelefoneDTO> toTelefoneDTOList(List<Telefone> telefones) {
        return telefones.stream()
                .map(this::toTelefoneDTO).toList();
    }

    public EnderecoDTO toEnderecoDTO(Endereco endereco) {
        return EnderecoDTO.builder()
                .id(endereco.getId())
                .rua(endereco.getRua())
                .numero(endereco.getNumero())
                .complemento(endereco.getComplemento())
                .cep(endereco.getCep())
                .cidade(endereco.getCidade())
                .estado(endereco.getEstado())
                .build();
    }

    public TelefoneDTO toTelefoneDTO(Telefone telefone) {
        return TelefoneDTO.builder()
                .id(telefone.getId())
                .numero(telefone.getNumero())
                .ddd(telefone.getDdd())
                .build();
    }

    public Usuario updateUsuario(UsuarioDTO dto, Usuario entity) {
        return Usuario.builder()
                .id(entity.getId())
                .nome(dto.getNome() != null ? dto.getNome() : entity.getNome())
                .email(dto.getEmail() != null ? dto.getEmail() : entity.getEmail())
                .senha(dto.getSenha() != null ? dto.getSenha() : entity.getSenha())
                .enderecos(entity.getEnderecos())
                .telefones(entity.getTelefones())
                .build();
    }

    public Endereco updateEndereco(EnderecoDTO dto, Endereco entity) {
        return Endereco.builder()
                .id(entity.getId())
                .rua(dto.getRua() != null ? dto.getRua() : entity.getRua())
                .numero(dto.getNumero() != null ? dto.getNumero() : entity.getNumero())
                .cep(dto.getCep() != null ? dto.getCep() : entity.getCep())
                .complemento(dto.getComplemento() != null ? dto.getComplemento() : entity.getComplemento())
                .cidade(dto.getCidade() != null ? dto.getCidade() : entity.getCidade())
                .estado(dto.getEstado() != null ? dto.getEstado() : entity.getEstado())
                .build();
    }

    public Telefone updateTelefone(TelefoneDTO dto, Telefone entity) {
        return Telefone.builder()
                .id(entity.getId())
                .numero(dto.getNumero() != null ? dto.getNumero() : entity.getNumero())
                .ddd(dto.getDdd() != null ? dto.getDdd() : entity.getDdd())
                .build();
    }

    public Endereco toEndereco(EnderecoDTO enderecoDTO, Long usuarioId) {
        return Endereco.builder()
                .rua(enderecoDTO.getRua())
                .numero(enderecoDTO.getNumero())
                .complemento(enderecoDTO.getComplemento())
                .cep(enderecoDTO.getCep())
                .cidade(enderecoDTO.getCidade())
                .estado(enderecoDTO.getEstado())
                .usuarioId(usuarioId)
                .build();
    }

    public Telefone toTelefone(TelefoneDTO telefoneDTO, Long usuarioId) {
        return Telefone.builder()
                .numero(telefoneDTO.getNumero())
                .ddd(telefoneDTO.getDdd())
                .usuarioId(usuarioId)
                .build();
    }
}
