package com.spring.team1.tiendamia.Models.productos;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "productos")
@Data
public class Productos {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categorias categoria;

    @ManyToOne
    @JoinColumn(name = "marca_id")
    private Marcas marca;

    private String nombre;
    private String slug;
    private String descripcion;
    private Boolean activo = true;
}
