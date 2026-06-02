package com.spring.team1.tiendamia.models.productos.carateristicas;

import com.spring.team1.tiendamia.models.productos.Producto;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "imagenes_producto")
@Data
public class ImagenesProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "orden", nullable = false)
    private Integer orden;

}
