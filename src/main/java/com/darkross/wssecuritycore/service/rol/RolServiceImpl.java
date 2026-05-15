package com.darkross.wssecuritycore.service.rol;

import com.darkross.wssecuritycore.dto.rol.RolRequestDto;
import com.darkross.wssecuritycore.dto.rol.RolResponseDto;
import com.darkross.wssecuritycore.entity.Rol;
import com.darkross.wssecuritycore.exception.Rol.RolDuplicatedException;
import com.darkross.wssecuritycore.exception.Rol.RolNotFoundException;
import com.darkross.wssecuritycore.mapper.RolMapper;
import com.darkross.wssecuritycore.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepository;
    private final RolMapper rolMapper;

    @Override
    public RolResponseDto createRol(RolRequestDto requestDto) {

        if (rolRepository.existsByRol(requestDto.getRol())) {
            throw new RolDuplicatedException();
        }

        Rol rol = rolMapper.toEntity(requestDto);
        rol = rolRepository.save(rol);

        return rolMapper.toResponseDto(rol);
    }

    @Override
    public RolResponseDto updateRol(Long id, RolRequestDto requestDto) {

        Rol rol = rolRepository.findById(id)
                .orElseThrow(RolNotFoundException::new);

        if (!rol.getRol().equals(requestDto.getRol())
                && rolRepository.existsByRol(requestDto.getRol())) {
            throw new RolDuplicatedException();
        }

        rolMapper.updateEntityFromDto(requestDto, rol);
        rol = rolRepository.save(rol);

        return rolMapper.toResponseDto(rol);
    }

    @Override
    @Transactional(readOnly = true)
    public RolResponseDto getRolById(Long id) {

        Rol rol = rolRepository.findById(id)
                .orElseThrow(RolNotFoundException::new);

        return rolMapper.toResponseDto(rol);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RolResponseDto> getAllRoles() {

        return rolRepository.findAll()
                .stream()
                .map(rolMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteRol(Long id) {

        Rol rol = rolRepository.findById(id)
                .orElseThrow(RolNotFoundException::new);

        rol.setEstado(false);
        rolRepository.save(rol);
    }

    @Override
    public RolResponseDto restoreRol(Long id) {

        Rol rol = rolRepository.findById(id)
                .orElseThrow(RolNotFoundException::new);

        rol.setEstado(true);
        rol = rolRepository.save(rol);

        return rolMapper.toResponseDto(rol);
    }
}