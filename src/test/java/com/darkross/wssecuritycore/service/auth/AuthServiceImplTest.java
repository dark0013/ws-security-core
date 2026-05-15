package com.darkross.wssecuritycore.service.auth;

import com.darkross.wssecuritycore.dto.auth.LoginRequestDto;
import com.darkross.wssecuritycore.dto.auth.LoginResponseDto;
import com.darkross.wssecuritycore.entity.Rol;
import com.darkross.wssecuritycore.entity.User;
import com.darkross.wssecuritycore.entity.UsuarioRol;
import com.darkross.wssecuritycore.repository.UserRepository;
import com.darkross.wssecuritycore.repository.UsuarioRolRepository;
import com.darkross.wssecuritycore.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para AuthServiceImpl")
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UsuarioRolRepository usuarioRolRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    private LoginRequestDto loginRequest;
    private User user;
    private Rol rol;
    private UsuarioRol usuarioRol;
    private Authentication authentication;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        // Preparar datos de prueba
        loginRequest = new LoginRequestDto();
        loginRequest.setUsernameOrEmail("admin");
        loginRequest.setPassword("password123");

        user = new User();
        user.setId(1L);
        user.setUsername("admin");
        user.setEmail("admin@example.com");
        user.setNombres("Administrador");
        user.setApellidos("Sistema");
        user.setPassword("$2a$10$encryptedpassword"); // BCrypt hash simulado

        rol = new Rol();
        rol.setId(1L);
        rol.setRol("ADMIN");

        usuarioRol = new UsuarioRol();
        usuarioRol.setUsuario(user);
        usuarioRol.setRol(rol);
        usuarioRol.setEstado(true);

        userDetails = org.springframework.security.core.userdetails.User
                .withUsername("admin")
                .password("$2a$10$encryptedpassword")
                .authorities(Arrays.asList(new SimpleGrantedAuthority("ADMIN")))
                .build();

        authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Test
    @DisplayName("Login exitoso - retorna JWT y datos completos del usuario")
    void testLogin_Success() {
        // Given
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtUtils.generateToken(authentication)).thenReturn("jwt.token.here");
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
        when(usuarioRolRepository.findByUsuario(user)).thenReturn(Arrays.asList(usuarioRol));

        // When
        LoginResponseDto response = authService.login(loginRequest);

        // Then
        assertNotNull(response);
        assertEquals("jwt.token.here", response.getToken());
        assertEquals("Bearer", response.getType());
        assertEquals(1L, response.getId());
        assertEquals("admin", response.getUsername());
        assertEquals("admin@example.com", response.getEmail());
        assertEquals("Administrador", response.getNombres());
        assertEquals("Sistema", response.getApellidos());
        assertEquals(Arrays.asList("ADMIN"), response.getRoles());

        verify(authenticationManager, times(1)).authenticate(any());
        verify(jwtUtils, times(1)).generateToken(authentication);
        verify(userRepository, times(1)).findByUsername("admin");
        verify(usuarioRolRepository, times(1)).findByUsuario(user);
    }

    @Test
    @DisplayName("Login - filtra solo roles activos")
    void testLogin_FiltersActiveRolesOnly() {
        // Given
        UsuarioRol inactiveUsuarioRol = new UsuarioRol();
        inactiveUsuarioRol.setUsuario(user);
        inactiveUsuarioRol.setRol(rol);
        inactiveUsuarioRol.setEstado(false); // Rol inactivo

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtUtils.generateToken(authentication)).thenReturn("jwt.token.here");
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
        when(usuarioRolRepository.findByUsuario(user)).thenReturn(Arrays.asList(inactiveUsuarioRol));

        // When
        LoginResponseDto response = authService.login(loginRequest);

        // Then
        assertNotNull(response);
        assertTrue(response.getRoles().isEmpty()); // No debe incluir roles inactivos
    }

    @Test
    @DisplayName("Login - usuario no encontrado después de autenticación lanza RuntimeException")
    void testLogin_UserNotFoundAfterAuthentication() {
        // Given
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userRepository.findByUsername("admin")).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.login(loginRequest);
        });

        assertEquals("Usuario no encontrado", exception.getMessage());
    }

    @Test
    @DisplayName("Login - usuario sin roles retorna lista vacía")
    void testLogin_UserWithoutRoles() {
        // Given
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtUtils.generateToken(authentication)).thenReturn("jwt.token.here");
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
        when(usuarioRolRepository.findByUsuario(user)).thenReturn(Arrays.asList());

        // When
        LoginResponseDto response = authService.login(loginRequest);

        // Then
        assertNotNull(response);
        assertTrue(response.getRoles().isEmpty());
    }
}
