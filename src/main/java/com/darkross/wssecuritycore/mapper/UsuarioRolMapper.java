package com.darkross.wssecuritycore.mapper;

import com.darkross.wssecuritycore.dto.usuariorol.UsuarioRolRequestDto;
import com.darkross.wssecuritycore.dto.usuariorol.UsuarioRolResponseDto;
import com.darkross.wssecuritycore.entity.UsuarioRol;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UsuarioRolMapper {

    @Mapping(source = "usuario.id", target = "usuarioId")
    @Mapping(source = "usuario.username", target = "usuarioUsername")
    @Mapping(source = "rol.id", target = "rolId")
    @Mapping(source = "rol.rol", target = "rolNombre")
    UsuarioRolResponseDto toResponseDto(UsuarioRol usuarioRol);

    @Mapping(target = "estado",
            expression = "java(requestDto.getEstado() != null ? requestDto.getEstado() : true)")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "rol", ignore = true)
    @Mapping(target = "usuarioCreacion", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "usuarioActualizacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    UsuarioRol toEntity(UsuarioRolRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "rol", ignore = true)
    @Mapping(target = "usuarioCreacion", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "usuarioActualizacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    void updateEntityFromDto(UsuarioRolRequestDto requestDto, @MappingTarget UsuarioRol usuarioRol);
}

