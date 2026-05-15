package com.darkross.wssecuritycore.service.auth;

import com.darkross.wssecuritycore.dto.auth.LoginRequestDto;
import com.darkross.wssecuritycore.dto.auth.LoginResponseDto;
import com.darkross.wssecuritycore.dto.auth.LoginRoleDto;
import com.darkross.wssecuritycore.entity.User;
import com.darkross.wssecuritycore.repository.UserRepository;
import com.darkross.wssecuritycore.repository.UsuarioRolRepository;
import com.darkross.wssecuritycore.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final UsuarioRolRepository usuarioRolRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequest) {

        String usernameOrEmail = loginRequest.getUsernameOrEmail().trim();
        String password = loginRequest.getPassword().trim();

        log.debug("Iniciando proceso de login para usuario: {}", usernameOrEmail);
        log.info("Password recibida: [{}]", password);

        try {

            // Buscar usuario para pruebas de validación
            User userTest = userRepository.findByUsername(usernameOrEmail)
                    .orElseGet(() -> userRepository.findByEmail(usernameOrEmail)
                            .orElseThrow(() ->
                                    new RuntimeException("Usuario no encontrado")));

            log.info("Hash almacenado en BD: {}", userTest.getPassword());

            boolean matches = passwordEncoder.matches(
                    password,
                    userTest.getPassword()
            );

            log.info("¿La contraseña coincide?: {}", matches);

            // Autenticación Spring Security
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            usernameOrEmail,
                            password
                    )
            );

            log.debug("Autenticación exitosa para usuario: {}", usernameOrEmail);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtUtils.generateToken(authentication);

            UserDetails userDetails =
                    (UserDetails) authentication.getPrincipal();

            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() ->
                            new RuntimeException("Usuario no encontrado"));

            log.debug("Usuario encontrado en BD: {}", user.getUsername());

            List<LoginRoleDto> roles = usuarioRolRepository.findByUsuario(user)
                    .stream()
                    .filter(ur -> ur.getEstado())
                    .map(ur -> new LoginRoleDto(ur.getRol().getId(), ur.getRol().getRol()))
                    .collect(Collectors.toList());

            log.debug("Roles obtenidos para usuario {}: {}",
                    user.getUsername(),
                    roles);

            LoginRoleDto firstRole = roles.isEmpty() ? null : roles.get(0);
            Long idRolRol = firstRole != null ? firstRole.getId() : null;
            String descripcionRol = firstRole != null ? firstRole.getDescripcion() : null;

            return new LoginResponseDto(
                    jwt,
                    "Bearer",
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getNombres(),
                    user.getApellidos(),
                    idRolRol,
                    descripcionRol
            );

        } catch (Exception e) {

            log.error(
                    "Error durante autenticación para usuario {}: {}",
                    usernameOrEmail,
                    e.getMessage()
            );

            throw e;
        }
    }
}