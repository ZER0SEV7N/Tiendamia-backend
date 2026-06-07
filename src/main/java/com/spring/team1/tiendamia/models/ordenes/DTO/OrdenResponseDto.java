package com.spring.team1.tiendamia.models.ordenes.DTO;

import lombok.Data;
import java.util.List;

@Data
public class OrdenResponseDto {
    
    private String id; // Ejemplo: "TM-000" + id
    private String fecha;
    private String estado;
    private String estadoColor;
    private String total;
    private List<ProductoDto> productos;

    @Data
    public static class ProductoDto {
        private String nombre;
        private Integer cantidad;
        private String tiendaOrigen;
        private String imagen; // Emoji o URL de imagen de la variación
    }
}
