package com.darkross.wssecuritycore.mapper;

import com.darkross.wssecuritycore.dto.rol.RolRequestDto;
import com.darkross.wssecuritycore.dto.rol.RolResponseDto;
import com.darkross.wssecuritycore.entity.Rol;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RolMapper {

    RolResponseDto toResponseDto(Rol rol);

    @Mapping(target = "estado",
            expression = "java(requestDto.getEstado() != null ? requestDto.getEstado() : true)")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuarioCreacion", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "usuarioActualizacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    Rol toEntity(RolRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuarioCreacion", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "usuarioActualizacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    void updateEntityFromDto(RolRequestDto requestDto, @MappingTarget Rol rol);
}
