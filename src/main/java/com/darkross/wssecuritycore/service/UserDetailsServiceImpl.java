package com.darkross.wssecuritycore.service;

import com.darkross.wssecuritycore.entity.User;
import com.darkross.wssecuritycore.repository.UserRepository;
import com.darkross.wssecuritycore.repository.UsuarioRolRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final UsuarioRolRepository usuarioRolRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String usernameOrEmail)
            throws UsernameNotFoundException {

        log.debug("Intentando cargar usuario por username/email: {}", usernameOrEmail);

        User user = userRepository.findByUsername(usernameOrEmail)
                .orElseGet(() -> userRepository.findByEmail(usernameOrEmail)
                        .orElseThrow(() ->
                                new UsernameNotFoundException(
                                        "Usuario no encontrado: " + usernameOrEmail
                                )));

        log.debug("Usuario encontrado: {}, estado: {}",
                user.getUsername(),
                user.getEstado());

        // Validar usuario activo
        if (!user.getEstado()) {

            log.warn("Usuario inactivo intentando login: {}", usernameOrEmail);

            throw new UsernameNotFoundException(
                    "Usuario inactivo: " + usernameOrEmail
            );
        }

        // Obtener roles activos
        List<String> roles = usuarioRolRepository.findByUsuario(user)
                .stream()
                .filter(ur -> ur.getEstado())
                .map(ur -> ur.getRol().getRol())
                .collect(Collectors.toList());

        log.debug("Roles encontrados para usuario {}: {}",
                user.getUsername(),
                roles);

        // Validar que tenga al menos un rol
        if (roles.isEmpty()) {

            log.warn("Usuario {} no tiene roles asignados",
                    user.getUsername());

            throw new UsernameNotFoundException(
                    "Usuario sin roles asignados"
            );
        }

        log.debug("Creando UserDetails para usuario: {}",
                user.getUsername());

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(roles.toArray(new String[0]))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}