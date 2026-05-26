package com.spring.team1.tiendamia.models.payload;

import lombok.Data;

@Data
public class LoginRequest {
    private String correo;
    private String password;
}
