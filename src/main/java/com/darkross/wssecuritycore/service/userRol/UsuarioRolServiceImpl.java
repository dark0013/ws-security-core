package com.darkross.wssecuritycore.service.userRol;

import com.darkross.wssecuritycore.dto.usuariorol.UsuarioRolRequestDto;
import com.darkross.wssecuritycore.dto.usuariorol.UsuarioRolResponseDto;
import com.darkross.wssecuritycore.entity.Rol;
import com.darkross.wssecuritycore.entity.User;
import com.darkross.wssecuritycore.entity.UsuarioRol;
import com.darkross.wssecuritycore.exception.Rol.RolNotFoundException;
import com.darkross.wssecuritycore.exception.User.UserNotFoundException;
import com.darkross.wssecuritycore.exception.UsuarioRol.UsuarioRolDuplicatedException;
import com.darkross.wssecuritycore.exception.UsuarioRol.UsuarioRolNotFoundException;
import com.darkross.wssecuritycore.mapper.UsuarioRolMapper;
import com.darkross.wssecuritycore.repository.RolRepository;
import com.darkross.wssecuritycore.repository.UserRepository;
import com.darkross.wssecuritycore.repository.UsuarioRolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioRolServiceImpl implements UsuarioRolService {

    private final UsuarioRolRepository usuarioRolRepository;
    private final UserRepository userRepository;
    private final RolRepository rolRepository;
    private final UsuarioRolMapper usuarioRolMapper;

    @Override
    public UsuarioRolResponseDto createUsuarioRol(UsuarioRolRequestDto requestDto) {

        User usuario = validateAndGetUsuario(requestDto.getUsuarioId());
        Rol rol = validateAndGetRol(requestDto.getRolId());

        if (usuarioRolRepository.existsByUsuarioAndRol(usuario, rol)) {
            throw new UsuarioRolDuplicatedException();
        }

        UsuarioRol usuarioRol = new UsuarioRol();
        usuarioRol.setUsuario(usuario);
        usuarioRol.setRol(rol);
        usuarioRol.setEstado(requestDto.getEstado() != null ? requestDto.getEstado() : true);

        usuarioRol = usuarioRolRepository.save(usuarioRol);

        return usuarioRolMapper.toResponseDto(usuarioRol);
    }

    @Override
    public UsuarioRolResponseDto updateUsuarioRol(Long id, UsuarioRolRequestDto requestDto) {

        UsuarioRol usuarioRol = usuarioRolRepository.findById(id)
                .orElseThrow(UsuarioRolNotFoundException::new);

        User usuario = validateAndGetUsuario(requestDto.getUsuarioId());
        Rol rol = validateAndGetRol(requestDto.getRolId());

        if (!usuarioRol.getUsuario().getId().equals(usuario.getId())
                || !usuarioRol.getRol().getId().equals(rol.getId())) {
            if (usuarioRolRepository.existsByUsuarioAndRol(usuario, rol)) {
                throw new UsuarioRolDuplicatedException();
            }
        }

        usuarioRol.setUsuario(usuario);
        usuarioRol.setRol(rol);
        usuarioRolMapper.updateEntityFromDto(requestDto, usuarioRol);
        usuarioRol = usuarioRolRepository.save(usuarioRol);

        return usuarioRolMapper.toResponseDto(usuarioRol);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioRolResponseDto getUsuarioRolById(Long id) {

        UsuarioRol usuarioRol = usuarioRolRepository.findById(id)
                .orElseThrow(UsuarioRolNotFoundException::new);

        return usuarioRolMapper.toResponseDto(usuarioRol);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioRolResponseDto> getAllUsuarioRoles() {

        return usuarioRolRepository.findAll()
                .stream()
                .map(usuarioRolMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioRolResponseDto> getUsuarioRolesByIdUsuario(Long idUsuario) {

        User usuario = userRepository.findById(idUsuario)
                .orElseThrow(UserNotFoundException::new);

        return usuarioRolRepository.findByUsuario(usuario)
                .stream()
                .map(usuarioRolMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioRolResponseDto> getUsuarioRolesByIdRol(Long idRol) {

        Rol rol = rolRepository.findById(idRol)
                .orElseThrow(RolNotFoundException::new);

        return usuarioRolRepository.findByRol(rol)
                .stream()
                .map(usuarioRolMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUsuarioRol(Long id) {

        UsuarioRol usuarioRol = usuarioRolRepository.findById(id)
                .orElseThrow(UsuarioRolNotFoundException::new);

        usuarioRol.setEstado(false);
        usuarioRolRepository.save(usuarioRol);
    }

    @Override
    public UsuarioRolResponseDto restoreUsuarioRol(Long id) {

        UsuarioRol usuarioRol = usuarioRolRepository.findById(id)
                .orElseThrow(UsuarioRolNotFoundException::new);

        usuarioRol.setEstado(true);
        usuarioRol = usuarioRolRepository.save(usuarioRol);

        return usuarioRolMapper.toResponseDto(usuarioRol);
    }

    /**
     * Valida que el usuario exista en la base de datos
     * 
     * @param usuarioId ID del usuario a validar
     * @return El objeto User si existe
     * @throws UserNotFoundException Si el usuario no existe
     */
    private User validateAndGetUsuario(Long usuarioId) {
        return userRepository.findById(usuarioId)
                .orElseThrow(UserNotFoundException::new);
    }

    /**
     * Valida que el rol exista en la base de datos
     * 
     * @param rolId ID del rol a validar
     * @return El objeto Rol si existe
     * @throws RolNotFoundException Si el rol no existe
     */
    private Rol validateAndGetRol(Long rolId) {
        return rolRepository.findById(rolId)
                .orElseThrow(RolNotFoundException::new);
    }
}

