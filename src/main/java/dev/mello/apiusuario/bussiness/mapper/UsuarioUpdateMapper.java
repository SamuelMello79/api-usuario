package dev.mello.apiusuario.bussiness.mapper;

import dev.mello.apiusuario.bussiness.dto.request.UsuarioRequestDTO;
import dev.mello.apiusuario.infrastructure.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UsuarioUpdateMapper {
    UsuarioUpdateMapper INSTANCE = Mappers.getMapper(UsuarioUpdateMapper.class);

    @Mapping(target = "id", ignore = true)
    // apenas para realizar o teste
    @Mapping(target = "enderecos", ignore = true)
    @Mapping(target = "telefones", ignore = true)
    Usuario updateUsuarioDromDTO(UsuarioRequestDTO usuarioRequestDTO, @MappingTarget Usuario usuario);
}
