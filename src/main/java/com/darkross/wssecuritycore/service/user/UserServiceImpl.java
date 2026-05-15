package com.darkross.wssecuritycore.service.user;

import com.darkross.wssecuritycore.dto.user.UserRequestDto;
import com.darkross.wssecuritycore.dto.user.UserResponseDto;
import com.darkross.wssecuritycore.dto.usuariorol.UsuarioRolRequestDto;
import com.darkross.wssecuritycore.entity.User;
import com.darkross.wssecuritycore.entity.Rol;
import com.darkross.wssecuritycore.exception.User.UserDuplicatedException;
import com.darkross.wssecuritycore.exception.User.UserNotFoundException;
import com.darkross.wssecuritycore.mapper.UserMapper;
import com.darkross.wssecuritycore.repository.RolRepository;
import com.darkross.wssecuritycore.repository.UserRepository;
import com.darkross.wssecuritycore.service.email.EmailService;
import com.darkross.wssecuritycore.service.userRol.UsuarioRolService;
import com.darkross.wssecuritycore.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EmailService emailService;
    private final UsuarioRolService usuarioRolService;
    private final RolRepository rolRepository;

    @Override
    public UserResponseDto createUser(UserRequestDto requestDto) {
        if (userRepository.existsByCedula(requestDto.getCedula())) {
            throw new UserDuplicatedException("La cédula ya está registrada");
        }
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new UserDuplicatedException("El email ya está registrado");
        }
        if (userRepository.existsByUsername(requestDto.getUsername())) {
            throw new UserDuplicatedException("El username ya está registrado");
        }

        // Generar contraseña aleatoria y hashearla con BCrypt
        PasswordUtils.PasswordResult passwordResult = PasswordUtils.generateAndHashPassword();

        User user = userMapper.toEntity(requestDto);
        user.setPassword(passwordResult.getHashedPassword()); // Guardar contraseña hasheada
        user = userRepository.save(user);

        // Asignar rol por defecto "USER" al nuevo usuario
        try {
            // Buscar rol por defecto "USER"
            Rol rolPorDefecto = rolRepository.findByRol("USER")
                    .orElseThrow(() -> new RuntimeException("Rol USER no encontrado en el sistema"));

            UsuarioRolRequestDto usuarioRolRequest = new UsuarioRolRequestDto();
            usuarioRolRequest.setUsuarioId(user.getId());
            usuarioRolRequest.setRolId(rolPorDefecto.getId());
            usuarioRolRequest.setEstado(true);

            usuarioRolService.createUsuarioRol(usuarioRolRequest);
            log.info("Rol USER asignado automáticamente al usuario: {}", user.getUsername());
        } catch (Exception e) {
            log.error("Error al asignar rol por defecto al usuario {}: {}", user.getUsername(), e.getMessage());
            // No lanzamos excepción para no impedir la creación del usuario
        }

        // Enviar email con credenciales (contraseña original)
        emailService.sendWelcomeEmail(user.getEmail(), user.getUsername(), passwordResult.getRawPassword());

        return userMapper.toResponseDto(user);
    }

    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto requestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        if (!user.getCedula().equals(requestDto.getCedula()) && userRepository.existsByCedula(requestDto.getCedula())) {
            throw new UserDuplicatedException("La cédula ya está registrada");
        }
        if (!user.getEmail().equals(requestDto.getEmail()) && userRepository.existsByEmail(requestDto.getEmail())) {
            throw new UserDuplicatedException("El email ya está registrado");
        }
        if (!user.getUsername().equals(requestDto.getUsername()) && userRepository.existsByUsername(requestDto.getUsername())) {
            throw new UserDuplicatedException("El username ya está registrado");
        }

        userMapper.updateEntityFromDto(requestDto, user);
        user = userRepository.save(user);
        return userMapper.toResponseDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        return userMapper.toResponseDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        user.setEstado(false);
        userRepository.save(user);
    }

    @Override
    public UserResponseDto restoreUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        user.setEstado(true);
        user = userRepository.save(user);
        return userMapper.toResponseDto(user);
    }
}
