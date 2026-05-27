package com.spring.team1.tiendamia.models.payload.producto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductoEditRequest {
    @NotNull(message = "La categoría es obligatoria")
    private Integer idCategoria;

    @NotNull(message = "La marca es obligatoria")
    private Integer idMarca;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El slug es obligatorio")
    private String slug;

    @NotBlank(message = "La URL de la imagen es obligatoria")
    private String imagenUrl;

    private String descripcion;
    private Boolean estado;
}
