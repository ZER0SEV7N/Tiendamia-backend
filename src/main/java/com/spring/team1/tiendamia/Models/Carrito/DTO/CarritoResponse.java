package com.spring.team1.tiendamia.Models.Carrito.DTO;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder({})
public class CarritoResponse {

    private Integer idCarrito;
    private Double tarifa;
    private Double envio;
    private Double total;

    private List<CarritoItemResponse> items;

    @Data
    @JsonPropertyOrder({})
    public static class CarritoItemResponse {
        private Integer idVariante;
        private String variacion;
        private Integer cantidad;
        private Double precio;
    }


}
