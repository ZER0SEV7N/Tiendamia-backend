package com.spring.team1.tiendamia.Models.Productos;

import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "categorias")
@Data
public class Categorias {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
    private String slug;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_padre_id")
    private Categorias categoriaPadre;

    @OneToMany(mappedBy = "categoriaPadre", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Categorias> subcategorias;
}
