package com.spring.team1.tiendamia.models.payload.usuario;

import java.util.List;
import lombok.Data;

@Data
public class UsuarioPerfilDto {
    private Integer id;
    private String nombres;
    private String apellidos;
    private String correo;
    private String telefono;
    private Boolean activo;
    private String rol;
    private List<DireccionDto> direcciones;

    @Data
    public static class DireccionDto {
        private Integer id;
        private String direccion;
        private String referencia;
        private boolean esPrincipal;
        private String distrito;
        private String provincia;
        private String departamento;
        private String createAt;
    }

}
