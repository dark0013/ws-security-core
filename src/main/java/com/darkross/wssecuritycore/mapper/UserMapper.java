package com.darkross.wssecuritycore.mapper;

import com.darkross.wssecuritycore.dto.user.UserRequestDto;
import com.darkross.wssecuritycore.dto.user.UserResponseDto;
import com.darkross.wssecuritycore.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDto toResponseDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuarioCreacion", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "usuarioActualizacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    User toEntity(UserRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuarioCreacion", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "usuarioActualizacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    void updateEntityFromDto(UserRequestDto requestDto, @MappingTarget User user);
}
