package com.darkross.wssecuritycore.service.impl;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para UsuarioRolServiceImpl - Validaciones")
class UsuarioRolServiceImplTest {

    @Mock
    private UsuarioRolRepository usuarioRolRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private UsuarioRolMapper usuarioRolMapper;

    @InjectMocks
    private UsuarioRolServiceImpl usuarioRolService;

    private User usuario;
    private Rol rol;
    private UsuarioRol usuarioRol;
    private UsuarioRolRequestDto requestDto;
    private UsuarioRolResponseDto responseDto;

    @BeforeEach
    void setUp() {
        // Preparar datos de prueba
        usuario = new User();
        usuario.setId(1L);
        usuario.setUsername("testuser");

        rol = new Rol();
        rol.setId(1L);
        rol.setRol("ADMIN");

        usuarioRol = new UsuarioRol();
        usuarioRol.setId(1L);
        usuarioRol.setUsuario(usuario);
        usuarioRol.setRol(rol);
        usuarioRol.setEstado(true);

        requestDto = new UsuarioRolRequestDto();
        requestDto.setUsuarioId(1L);
        requestDto.setRolId(1L);
        requestDto.setEstado(true);

        responseDto = new UsuarioRolResponseDto();
        responseDto.setId(1L);
        responseDto.setUsuarioId(1L);
        responseDto.setRolId(1L);
        responseDto.setEstado(true);
    }

    // TESTS DE CREACIÓN
    @Test
    @DisplayName("Crear UsuarioRol - Validación exitosa de usuario y rol")
    void testCreateUsuarioRol_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol));
        when(usuarioRolRepository.existsByUsuarioAndRol(usuario, rol)).thenReturn(false);
        when(usuarioRolRepository.save(any())).thenReturn(usuarioRol);
        when(usuarioRolMapper.toResponseDto(usuarioRol)).thenReturn(responseDto);

        UsuarioRolResponseDto result = usuarioRolService.createUsuarioRol(requestDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userRepository, times(1)).findById(1L);
        verify(rolRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Crear UsuarioRol - Lanza UserNotFoundException cuando usuario no existe")
    void testCreateUsuarioRol_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            usuarioRolService.createUsuarioRol(requestDto);
        });

        verify(userRepository, times(1)).findById(1L);
        verify(rolRepository, never()).findById(anyLong());
        verify(usuarioRolRepository, never()).save(any());
    }

    @Test
    @DisplayName("Crear UsuarioRol - Lanza RolNotFoundException cuando rol no existe")
    void testCreateUsuarioRol_RolNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(rolRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RolNotFoundException.class, () -> {
            usuarioRolService.createUsuarioRol(requestDto);
        });

        verify(userRepository, times(1)).findById(1L);
        verify(rolRepository, times(1)).findById(1L);
        verify(usuarioRolRepository, never()).save(any());
    }

    @Test
    @DisplayName("Crear UsuarioRol - Lanza UsuarioRolDuplicatedException cuando ya existe la asignación")
    void testCreateUsuarioRol_DuplicateExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol));
        when(usuarioRolRepository.existsByUsuarioAndRol(usuario, rol)).thenReturn(true);

        assertThrows(UsuarioRolDuplicatedException.class, () -> {
            usuarioRolService.createUsuarioRol(requestDto);
        });

        verify(userRepository, times(1)).findById(1L);
        verify(rolRepository, times(1)).findById(1L);
        verify(usuarioRolRepository, never()).save(any());
    }

    // TESTS DE ACTUALIZACIÓN
    @Test
    @DisplayName("Actualizar UsuarioRol - Validación exitosa")
    void testUpdateUsuarioRol_Success() {
        when(usuarioRolRepository.findById(1L)).thenReturn(Optional.of(usuarioRol));
        when(userRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol));
        when(usuarioRolRepository.existsByUsuarioAndRol(usuario, rol)).thenReturn(false);
        when(usuarioRolRepository.save(any())).thenReturn(usuarioRol);
        when(usuarioRolMapper.toResponseDto(usuarioRol)).thenReturn(responseDto);

        UsuarioRolResponseDto result = usuarioRolService.updateUsuarioRol(1L, requestDto);

        assertNotNull(result);
        verify(usuarioRolRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(rolRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Actualizar UsuarioRol - Lanza UsuarioRolNotFoundException si no existe el registro")
    void testUpdateUsuarioRol_UsuarioRolNotFound() {
        when(usuarioRolRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UsuarioRolNotFoundException.class, () -> {
            usuarioRolService.updateUsuarioRol(1L, requestDto);
        });

        verify(usuarioRolRepository, times(1)).findById(1L);
        verify(userRepository, never()).findById(anyLong());
    }

    @Test
    @DisplayName("Actualizar UsuarioRol - Lanza UserNotFoundException cuando usuario no existe")
    void testUpdateUsuarioRol_UserNotFound() {
        when(usuarioRolRepository.findById(1L)).thenReturn(Optional.of(usuarioRol));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            usuarioRolService.updateUsuarioRol(1L, requestDto);
        });

        verify(usuarioRolRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Actualizar UsuarioRol - Lanza RolNotFoundException cuando rol no existe")
    void testUpdateUsuarioRol_RolNotFound() {
        when(usuarioRolRepository.findById(1L)).thenReturn(Optional.of(usuarioRol));
        when(userRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(rolRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RolNotFoundException.class, () -> {
            usuarioRolService.updateUsuarioRol(1L, requestDto);
        });

        verify(usuarioRolRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(rolRepository, times(1)).findById(1L);
    }

    // TESTS DE CONSULTAS
    @Test
    @DisplayName("Obtener UsuarioRoles por Usuario - Valida existencia del usuario")
    void testGetUsuarioRolesByIdUsuario_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            usuarioRolService.getUsuarioRolesByIdUsuario(1L);
        });

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Obtener UsuarioRoles por Rol - Valida existencia del rol")
    void testGetUsuarioRolesByIdRol_RolNotFound() {
        when(rolRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RolNotFoundException.class, () -> {
            usuarioRolService.getUsuarioRolesByIdRol(1L);
        });

        verify(rolRepository, times(1)).findById(1L);
    }
}

