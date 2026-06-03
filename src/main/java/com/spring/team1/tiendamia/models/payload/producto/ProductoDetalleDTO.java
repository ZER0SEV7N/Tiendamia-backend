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
    private Integer categoriaId;
    private Integer marcaId;
    private String nombreCategoria;
    private String nombreMarca;    
    private List<String> galeria;
    private List<AtributoOpcionesDTO> atributos;
    private List<VariacionDTO> variaciones;

    // --- CLASES ANIDADAS ---
    @Data
    public static class AtributoOpcionesDTO {
        private String nombre; // Ej: "Color"
        private List<String> opciones; // Ej: ["Rojo", "Azul", "Negro"]
    }

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
        private String atributoNombre;
        private String valorTexto;
    }
}