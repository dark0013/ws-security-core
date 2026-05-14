package com.darkross.wssecuritycore.service.impl;

import com.darkross.wssecuritycore.dto.UserRequestDto;
import com.darkross.wssecuritycore.dto.UserResponseDto;
import com.darkross.wssecuritycore.entity.User;
import com.darkross.wssecuritycore.exception.User.UserDuplicatedException;
import com.darkross.wssecuritycore.exception.User.UserNotFoundException;
import com.darkross.wssecuritycore.mapper.UserMapper;
import com.darkross.wssecuritycore.repository.UserRepository;
import com.darkross.wssecuritycore.service.UserService;
import com.darkross.wssecuritycore.service.EmailService;
import com.darkross.wssecuritycore.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EmailService emailService;

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

        // Generar contraseña aleatoria y hashearla con MD5
        PasswordUtils.PasswordResult passwordResult = PasswordUtils.generateAndHashPassword();

        User user = userMapper.toEntity(requestDto);
        user.setPassword(passwordResult.getHashedPassword()); // Guardar contraseña hasheada
        user = userRepository.save(user);

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
