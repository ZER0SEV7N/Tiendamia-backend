package com.spring.team1.tiendamia.models.productos;

import java.util.List;

import com.spring.team1.tiendamia.models.productos.carateristicas.Categorias;
import com.spring.team1.tiendamia.models.productos.carateristicas.ImagenesProducto;
import com.spring.team1.tiendamia.models.productos.carateristicas.Marcas;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "producto")
@Data
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_Categoria")
    private Categorias categoria;

    @ManyToOne
    @JoinColumn(name = "id_Marca")
    private Marcas marca;
    private String imagen_url;
    private String nombre;
    private String slug;
    private String descripcion;
    private Boolean estado = true;

    @OneToMany(mappedBy = "producto", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<VariacionesProducto> variaciones;

    @OneToMany(mappedBy = "producto", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ImagenesProducto> imagenes;
}
