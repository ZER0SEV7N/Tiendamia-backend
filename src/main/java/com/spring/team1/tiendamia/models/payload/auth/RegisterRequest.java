package com.spring.team1.tiendamia.models.payload.auth;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "El campo nombres no puede estar vacío")
    private String nombres;
    
    @NotBlank(message = "El campo apellidos no puede estar vacío")
    private String apellidos;

    @NotBlank(message = "El campo correo no puede estar vacío")
    @Email(message = "El campo correo debe ser una dirección de correo válida")
    private String correo;

    private String telefono;
    @NotBlank(message = "El campo password no puede estar vacío")
    @Size(min = 6, message = "El campo password debe tener al menos 6 caracteres")
    private String password;
}
