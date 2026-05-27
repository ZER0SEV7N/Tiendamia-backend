package com.spring.team1.tiendamia.models.carrito.DTO;

import java.util.List;

import lombok.Data;

@Data
public class CarritoRequest {

    private Integer idUsuario;

    private List<CarritoItemRequest> items;

    @Data
    public static class CarritoItemRequest {
        private Integer idVariante;
        private Integer cantidad;
    }
}
