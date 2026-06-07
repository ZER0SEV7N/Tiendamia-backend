package com.spring.team1.tiendamia.util;

import lombok.Data;

@Data
public class Response <T> {
    private Boolean success;
    private String mensaje;
    private T data;

    public Response(Boolean success, String mensaje, T data) {
        this.success = success;
        this.mensaje = mensaje;
        this.data = data;
    }

}
