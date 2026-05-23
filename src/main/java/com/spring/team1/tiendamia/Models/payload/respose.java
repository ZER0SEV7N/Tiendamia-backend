package com.spring.team1.tiendamia.Models.payload;

import lombok.Data;

@Data
public class respose <T> {
    private Boolean success;
    private String mensaje;
    private T data;

    public respose(Boolean success, String mensaje, T data) {
        this.success = success;
        this.mensaje = mensaje;
        this.data = data;
    }

}
