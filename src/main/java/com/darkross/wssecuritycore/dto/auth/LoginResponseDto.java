package com.darkross.wssecuritycore.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {

    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private String nombres;
    private String apellidos;
    private List<String> roles;
}
