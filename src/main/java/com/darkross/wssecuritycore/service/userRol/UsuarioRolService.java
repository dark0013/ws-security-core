package com.darkross.wssecuritycore.service.userRol;

import com.darkross.wssecuritycore.dto.usuariorol.UsuarioRolRequestDto;
import com.darkross.wssecuritycore.dto.usuariorol.UsuarioRolResponseDto;

import java.util.List;

public interface UsuarioRolService {

    UsuarioRolResponseDto createUsuarioRol(UsuarioRolRequestDto requestDto);

    UsuarioRolResponseDto updateUsuarioRol(Long id, UsuarioRolRequestDto requestDto);

    UsuarioRolResponseDto getUsuarioRolById(Long id);

    List<UsuarioRolResponseDto> getAllUsuarioRoles();

    List<UsuarioRolResponseDto> getUsuarioRolesByIdUsuario(Long idUsuario);

    List<UsuarioRolResponseDto> getUsuarioRolesByIdRol(Long idRol);

    void deleteUsuarioRol(Long id);

    UsuarioRolResponseDto restoreUsuarioRol(Long id);
}

