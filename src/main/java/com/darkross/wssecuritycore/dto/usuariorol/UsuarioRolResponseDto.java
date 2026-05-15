package com.darkross.wssecuritycore.dto.usuariorol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRolResponseDto {

    private Long id;
    private Long usuarioId;
    private String usuarioUsername;
    private Long rolId;
    private String rolNombre;
    private Boolean estado;
    private String usuarioCreacion;
    private LocalDateTime fechaCreacion;
    private String usuarioActualizacion;
    private LocalDateTime fechaActualizacion;
}

