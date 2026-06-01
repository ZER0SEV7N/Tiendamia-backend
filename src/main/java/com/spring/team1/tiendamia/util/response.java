package com.spring.team1.tiendamia.util;

import lombok.Data;

@Data
public class response <T> {
    private Boolean success;
    private String mensaje;
    private T data;

    public response(Boolean success, String mensaje, T data) {
        this.success = success;
        this.mensaje = mensaje;
        this.data = data;
    }

}
