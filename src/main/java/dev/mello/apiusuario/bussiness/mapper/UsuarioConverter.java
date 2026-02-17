package dev.mello.apiusuario.bussiness.mapper;

import dev.mello.apiusuario.bussiness.dto.request.EnderecoRequestDTO;
import dev.mello.apiusuario.bussiness.dto.request.TelefoneRequestDTO;
import dev.mello.apiusuario.bussiness.dto.request.UsuarioRequestDTO;
import dev.mello.apiusuario.bussiness.dto.response.EnderecoResponseDTO;
import dev.mello.apiusuario.bussiness.dto.response.TelefoneResponseDTO;
import dev.mello.apiusuario.bussiness.dto.response.UsuarioResponseDTO;
import dev.mello.apiusuario.infrastructure.entity.Endereco;
import dev.mello.apiusuario.infrastructure.entity.Telefone;
import dev.mello.apiusuario.infrastructure.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsuarioConverter {
    public Usuario toEntity(UsuarioRequestDTO usuarioRequestDTO) {
        return Usuario.builder()
                .nome(usuarioRequestDTO.nome())
                .email(usuarioRequestDTO.email())
                .senha(usuarioRequestDTO.senha())
                .enderecos(toEnderecoList(usuarioRequestDTO.enderecos()))
                .telefones(toTelefoneList(usuarioRequestDTO.telefones()))
                .build();
    }

    public UsuarioResponseDTO toDto(Usuario usuario) {
        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .senha(usuario.getSenha())
                .enderecos(toEnderecoDTOList(usuario.getEnderecos()))
                .telefones(toTelefoneDTOList(usuario.getTelefones()))
                .build();
    }

    private List<Endereco> toEnderecoList(List<EnderecoRequestDTO> enderecosDTO) {
        return enderecosDTO.stream()
                .map(this::toEndereco).toList();
    }

    private List<Telefone> toTelefoneList(List<TelefoneRequestDTO> telefonesDTO) {
        return telefonesDTO.stream()
                .map(this::toTelefone).toList();
    }

    public Telefone toTelefone(TelefoneRequestDTO telefoneRequestDTO) {
        return Telefone.builder()
                .numero(telefoneRequestDTO.numero())
                .ddd(telefoneRequestDTO.ddd())
                .build();
    }

    public Endereco toEndereco(EnderecoRequestDTO enderecoRequestDTO) {
        return Endereco.builder()
                .rua(enderecoRequestDTO.rua())
                .numero(enderecoRequestDTO.numero())
                .complemento(enderecoRequestDTO.complemento())
                .cep(enderecoRequestDTO.cep())
                .cidade(enderecoRequestDTO.cidade())
                .estado(enderecoRequestDTO.estado())
                .build();
    }

    private List<EnderecoResponseDTO> toEnderecoDTOList(List<Endereco> enderecos) {
        return enderecos.stream()
                .map(this::toEnderecoDTO).toList();
    }

    private List<TelefoneResponseDTO> toTelefoneDTOList(List<Telefone> telefones) {
        return telefones.stream()
                .map(this::toTelefoneDTO).toList();
    }

    public EnderecoResponseDTO toEnderecoDTO(Endereco endereco) {
        return EnderecoResponseDTO.builder()
                .id(endereco.getId())
                .rua(endereco.getRua())
                .numero(endereco.getNumero())
                .complemento(endereco.getComplemento())
                .cidade(endereco.getCidade())
                .estado(endereco.getEstado())
                .cep(endereco.getCep())
                .build();
    }

    public TelefoneResponseDTO toTelefoneDTO(Telefone telefone) {
        return TelefoneResponseDTO.builder()
                .id(telefone.getId())
                .numero(telefone.getNumero())
                .ddd(telefone.getDdd())
                .build();
    }

    public Usuario updateUsuario(UsuarioRequestDTO dto, Usuario entity, String password) {
        return Usuario.builder()
                .id(entity.getId())
                .nome(dto.nome() != null ? dto.nome() : entity.getNome())
                .email(dto.email() != null ? dto.email() : entity.getEmail())
                .senha(dto.senha() != null ? password : entity.getSenha())
                .enderecos(entity.getEnderecos())
                .telefones(entity.getTelefones())
                .build();
    }

    public Endereco updateEndereco(EnderecoRequestDTO dto, Endereco entity) {
        return Endereco.builder()
                .id(entity.getId())
                .rua(dto.rua() != null ? dto.rua() : entity.getRua())
                .numero(dto.numero() != null ? dto.numero() : entity.getNumero())
                .cep(dto.cep() != null ? dto.cep() : entity.getCep())
                .complemento(dto.complemento() != null ? dto.complemento() : entity.getComplemento())
                .cidade(dto.cidade() != null ? dto.cidade() : entity.getCidade())
                .estado(dto.estado() != null ? dto.estado() : entity.getEstado())
                .build();
    }

    public Telefone updateTelefone(TelefoneRequestDTO dto, Telefone entity) {
        return Telefone.builder()
                .id(entity.getId())
                .numero(dto.numero() != null ? dto.numero() : entity.getNumero())
                .ddd(dto.ddd() != null ? dto.ddd() : entity.getDdd())
                .build();
    }

    public Endereco toEndereco(EnderecoRequestDTO enderecoRequestDTO, Long usuarioId) {
        return Endereco.builder()
                .rua(enderecoRequestDTO.rua())
                .numero(enderecoRequestDTO.numero())
                .complemento(enderecoRequestDTO.complemento())
                .cep(enderecoRequestDTO.cep())
                .cidade(enderecoRequestDTO.cidade())
                .estado(enderecoRequestDTO.estado())
                .usuarioId(usuarioId)
                .build();
    }

    public Telefone toTelefone(TelefoneRequestDTO telefoneRequestDTO, Long usuarioId) {
        return Telefone.builder()
                .numero(telefoneRequestDTO.numero())
                .ddd(telefoneRequestDTO.ddd())
                .usuarioId(usuarioId)
                .build();
    }
}
