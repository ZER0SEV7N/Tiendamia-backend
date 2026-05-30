package com.spring.team1.tiendamia.models.payload.producto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ProductoRequest {
    @NotBlank(message = "El nombre del producto es obligatorio")
    private String nombre;
    @NotBlank(message = "El slug es obligatorio")
    @Pattern(
        regexp = "^[a-z0-9-]+$", 
        message = "El slug solo debe contener letras minúsculas, números y guiones (ej: mi-marca-123)"
    )
    private String slug;
    @NotBlank(message = "La descripción del producto es obligatoria")
    private String descripcion;
    private String imagenUrl;
    private Integer categoriaId;
    private Integer marcaId;
    private List<VariacionInicialRequest> variaciones; // Lista de variaciones iniciales para el producto

    @Data
    public static class VariacionInicialRequest {
        private String codigoInventario;
        private Double precio;
        private Integer stock;
        private String imagenUrl;
        private List<CaracteristicaRequest> caracteristicas; // Lista de características para esta variación (ej: Color: Rojo, Almacenamiento: 128GB)
    }

    @Data
    public static class CaracteristicaRequest {
        private String atributoNombre; // Ej: "Color", "Almacenamiento", "Talla"
        private String valorTexto;     // Ej: "Rojo", "128GB", "M"
    }
}
