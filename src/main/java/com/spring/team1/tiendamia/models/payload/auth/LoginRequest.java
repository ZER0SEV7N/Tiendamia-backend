package com.spring.team1.tiendamia.models.payload.auth;

import lombok.Data;

@Data
public class LoginRequest {
    private String correo;
    private String password;
}
