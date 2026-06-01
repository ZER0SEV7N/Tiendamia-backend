package com.spring.team1.tiendamia.models.payload.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "El campo correo no puede estar vacío")
    @Email(message = "El campo correo debe ser una dirección de correo válida")
    private String correo;
    
    @NotBlank(message = "El campo password no puede estar vacío")
    private String password;
}
