package com.darkross.wssecuritycore.dto.rol;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolRequestDto {

    @NotBlank(message = "El rol es obligatorio")
    @Size(min = 2, max = 50, message = "El rol debe tener entre 2 y 50 caracteres")
    private String rol;

    private Boolean estado = true;
}
