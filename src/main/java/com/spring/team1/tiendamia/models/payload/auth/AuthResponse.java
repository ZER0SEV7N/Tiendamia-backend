package com.spring.team1.tiendamia.models.payload.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String correo;
    private String nombres;
    private String rol;
}
