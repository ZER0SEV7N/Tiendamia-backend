package com.spring.team1.tiendamia.models.payload.producto;

import lombok.Data;

@Data
public class ProductoList {
    private Integer id;
    private String nombre;
    private String slug;
    private String imagenUrl;
    private String descripcion;
    private String nombreCategoria;
    private String nombreMarca;
    private Boolean estado;
}
