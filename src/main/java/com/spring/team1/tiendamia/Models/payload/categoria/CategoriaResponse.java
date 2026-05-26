package com.spring.team1.tiendamia.Models.payload.categoria;

import java.util.List;

import lombok.Data;

@Data
public class CategoriaResponse {
    private Integer id;
    private String nombre;
    private String slug;
    private Integer idCategoriaPadre;

    private List<CategoriaResponse> subcategorias;
}
