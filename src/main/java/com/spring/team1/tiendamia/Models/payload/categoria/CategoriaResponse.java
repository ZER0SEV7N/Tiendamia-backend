package com.spring.team1.tiendamia.Models.payload.categoria;

import java.util.List;

import lombok.Data;

@Data
public class CategoriaResponse {
    private Long id;
    private String nombre;
    private String slug;
    private Long idCategoriaPadre;

    private List<CategoriaResponse> subcategorias;
}
