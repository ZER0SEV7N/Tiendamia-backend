package com.spring.team1.tiendamia.models.payload.auth;

import lombok.Data;

@Data
public class CambiarPasswordRequest {
    private String token; // Token que llego por email
    private String nuevaPassword;
}
