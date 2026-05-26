package com.spring.team1.tiendamia.models.payload;

import lombok.Data;

@Data
public class CambiarPasswordRequest {
    private String token; // Token que llego por email
    private String nuevaPassword;
}
