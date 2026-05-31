package com.spring.team1.tiendamia.models.payload.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CambiarPasswordRequest {
    private String token; // Token que llego por email
    @NotBlank(message = "El campo nuevaPassword no puede estar vacío")
    @Size(min = 6, message = "El campo nuevaPassword debe tener al menos 6 caracteres")
    private String nuevaPassword;
}
