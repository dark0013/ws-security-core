package com.darkross.wssecuritycore.controller;

import com.darkross.wssecuritycore.dto.usuariorol.UsuarioRolRequestDto;
import com.darkross.wssecuritycore.dto.usuariorol.UsuarioRolResponseDto;
import com.darkross.wssecuritycore.service.userRol.UsuarioRolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuario-roles")
@RequiredArgsConstructor
public class UsuarioRolController {

    private final UsuarioRolService usuarioRolService;

    @PostMapping
    public ResponseEntity<UsuarioRolResponseDto> createUsuarioRol(@Valid @RequestBody UsuarioRolRequestDto requestDto) {
        UsuarioRolResponseDto response = usuarioRolService.createUsuarioRol(requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioRolResponseDto> updateUsuarioRol(@PathVariable Long id, @Valid @RequestBody UsuarioRolRequestDto requestDto) {
        UsuarioRolResponseDto response = usuarioRolService.updateUsuarioRol(id, requestDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioRolResponseDto> getUsuarioRolById(@PathVariable Long id) {
        UsuarioRolResponseDto response = usuarioRolService.getUsuarioRolById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioRolResponseDto>> getAllUsuarioRoles() {
        List<UsuarioRolResponseDto> response = usuarioRolService.getAllUsuarioRoles();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<UsuarioRolResponseDto>> getUsuarioRolesByIdUsuario(@PathVariable Long idUsuario) {
        List<UsuarioRolResponseDto> response = usuarioRolService.getUsuarioRolesByIdUsuario(idUsuario);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/rol/{idRol}")
    public ResponseEntity<List<UsuarioRolResponseDto>> getUsuarioRolesByIdRol(@PathVariable Long idRol) {
        List<UsuarioRolResponseDto> response = usuarioRolService.getUsuarioRolesByIdRol(idRol);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuarioRol(@PathVariable Long id) {
        usuarioRolService.deleteUsuarioRol(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<UsuarioRolResponseDto> restoreUsuarioRol(@PathVariable Long id) {
        UsuarioRolResponseDto response = usuarioRolService.restoreUsuarioRol(id);
        return ResponseEntity.ok(response);
    }
}

