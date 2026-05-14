package com.darkross.wssecuritycore.service;

import com.darkross.wssecuritycore.dto.rol.RolRequestDto;
import com.darkross.wssecuritycore.dto.rol.RolResponseDto;

import java.util.List;

public interface RolService {

    RolResponseDto createRol(RolRequestDto requestDto);

    RolResponseDto updateRol(Long id, RolRequestDto requestDto);

    RolResponseDto getRolById(Long id);

    List<RolResponseDto> getAllRoles();

    void deleteRol(Long id);

    RolResponseDto restoreRol(Long id);
}
