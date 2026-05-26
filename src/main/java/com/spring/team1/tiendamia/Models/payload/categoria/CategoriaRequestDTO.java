package com.spring.team1.tiendamia.Models.payload.categoria;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoriaRequestDTO {
    @NotBlank(message = "El nombre de la categoría es obligatorio")
    private String nombre;
    @NotBlank(message = "El slug de la categoría es obligatorio")
    private String slug;
    private Long id_categoria_padre = null;
}
