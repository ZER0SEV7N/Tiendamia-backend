package com.spring.team1.tiendamia.models.payload.producto;

import java.util.List;

import lombok.Data;

@Data
public class ProductoDetalleDTO {
    private Integer id;
    private String nombre;
    private String slug;
    private String imagenUrl;
    private String descripcion;
    private String nombreCategoria;
    private String nombreMarca;
    private List<VariacionDTO> variaciones;

    @Data
    public static class VariacionDTO {
        private Integer id;
        private String codigoInventario;
        private Double precio;
        private Integer stock;
        private String imagenUrl;
        private List<CaracteristicaDTO> caracteristicas;
    }

    @Data
    public static class CaracteristicaDTO {
        private String atributo;
        private String valor;
    }
}
