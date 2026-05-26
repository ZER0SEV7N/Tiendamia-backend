package com.spring.team1.tiendamia.Models.productos;

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
    private Productos producto;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "orden", nullable = false)
    private Integer orden;

}
