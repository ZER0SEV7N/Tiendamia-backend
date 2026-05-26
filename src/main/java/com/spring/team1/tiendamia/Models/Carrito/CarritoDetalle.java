package com.spring.team1.tiendamia.Models.Carrito;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.spring.team1.tiendamia.Models.Productos.VariacionesProducto;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "carrito_detalle")
@Data
public class CarritoDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "carrito_id", nullable = false)
    @JsonBackReference
    private Carrito carrito;

    @ManyToOne
    @JoinColumn(name = "variacion_id", nullable = false)
    private VariacionesProducto variacion;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Double precio;
}
