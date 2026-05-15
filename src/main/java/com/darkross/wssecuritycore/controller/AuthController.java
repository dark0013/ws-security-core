package com.darkross.wssecuritycore.controller;

import com.darkross.wssecuritycore.dto.auth.LoginRequestDto;
import com.darkross.wssecuritycore.dto.auth.LoginResponseDto;
import com.darkross.wssecuritycore.exception.AuthException;
import com.darkross.wssecuritycore.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        try {
            LoginResponseDto response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException | UsernameNotFoundException e) {
            throw new AuthException("Credenciales inválidas");
        }
    }
}
