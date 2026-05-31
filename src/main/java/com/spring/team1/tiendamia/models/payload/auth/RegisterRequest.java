package com.spring.team1.tiendamia.models.payload.auth;

import lombok.Data;

@Data
public class RegisterRequest {
    private String nombres;
    private String apellidos;
    private String correo;
    private String telefono;
    private String password;
}
