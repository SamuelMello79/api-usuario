package dev.mello.apiusuario.bussiness.mapper;

import dev.mello.apiusuario.bussiness.dto.response.UsuarioResponseDTO;
import dev.mello.apiusuario.infrastructure.entity.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    UsuarioResponseDTO paraUsuarioDTO(Usuario usuario);
}
