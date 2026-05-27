package com.spring.team1.tiendamia.models.payload.producto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VariacionRequest {
    @NotBlank(message = "El código de inventario (SKU) es obligatorio")
    private String codigoInventario;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private Double precio;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    @NotBlank(message = "La URL de la imagen es obligatoria")
    private String imagenUrl;

    @NotEmpty(message = "La variación debe tener al menos una característica (Atributo-Valor)")
    @Valid
    private List<CaracteristicaRequest> caracteristicas;

    @Data
    public static class CaracteristicaRequest {
        @NotBlank(message = "El nombre del atributo es obligatorio (Ej: Color)")
        private String atributoNombre;

        @NotBlank(message = "El valor del atributo es obligatorio (Ej: Rojo)")
        private String valorTexto;
    }
}
