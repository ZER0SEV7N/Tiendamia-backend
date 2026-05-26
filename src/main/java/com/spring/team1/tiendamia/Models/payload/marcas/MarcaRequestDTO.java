package com.spring.team1.tiendamia.models.payload.marcas;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class MarcaRequestDTO {
    @NotBlank(message = "El nombre de la marca es obligatorio")
    private String nombre;    

    // El slug es obligatorio y debe seguir un formato específico (solo letras minúsculas, números y guiones)
    @NotBlank(message = "El slug es obligatorio")
    @Pattern(
        regexp = "^[a-z0-9-]+$", 
        message = "El slug solo debe contener letras minúsculas, números y guiones (ej: mi-marca-123)"
    )
    private String slug;

    // La imagen de logo es opcional, pero si se proporciona, debe ser una URL válida que termine con una extensión de imagen común
    @Pattern(
        regexp = "^$|.*\\.(jpg|jpeg|png|webp|svg)$", 
        message = "La imagen de logo debe ser una extensión válida (jpg, jpeg, png, webp, svg)"
    )
    private String imagen_logo;

    // La imagen de banner es opcional, pero si se proporciona, debe ser una URL válida que termine con una extensión de imagen común
    @Pattern(
        regexp = "^$|.*\\.(jpg|jpeg|png|webp|svg)$", 
        message = "La imagen de banner debe ser una extensión válida (jpg, jpeg, png, webp, svg)"
    )
    private String imagen_banner;

    @NotBlank(message = "La descripción de la marca es obligatoria")
    private String descripcion;

    @NotNull(message = "El estado destacada es obligatorio")
    private Boolean destacada = false;

    @NotNull(message = "El estado activo es obligatorio")
    private Boolean activo = true;
}
