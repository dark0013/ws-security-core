package com.darkross.wssecuritycore.controller;

import com.darkross.wssecuritycore.dto.RolRequestDto;
import com.darkross.wssecuritycore.dto.RolResponseDto;
import com.darkross.wssecuritycore.service.RolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RolController {

    private final RolService rolService;

    @PostMapping
    public ResponseEntity<RolResponseDto> createRol(@Valid @RequestBody RolRequestDto requestDto) {
        RolResponseDto response = rolService.createRol(requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RolResponseDto> updateRol(@PathVariable Long id, @Valid @RequestBody RolRequestDto requestDto) {
        RolResponseDto response = rolService.updateRol(id, requestDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolResponseDto> getRolById(@PathVariable Long id) {
        RolResponseDto response = rolService.getRolById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<RolResponseDto>> getAllRoles() {
        List<RolResponseDto> response = rolService.getAllRoles();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRol(@PathVariable Long id) {
        rolService.deleteRol(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<RolResponseDto> restoreRol(@PathVariable Long id) {
        RolResponseDto response = rolService.restoreRol(id);
        return ResponseEntity.ok(response);
    }
}
