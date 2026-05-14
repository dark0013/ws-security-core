package com.darkross.wssecuritycore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolResponseDto {

    private Long id;
    private String rol;
    private Boolean estado;
    private String usuarioCreacion;
    private LocalDateTime fechaCreacion;
    private String usuarioActualizacion;
    private LocalDateTime fechaActualizacion;
}
