package com.spring.team1.tiendamia.models.carrito;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.spring.team1.tiendamia.models.productos.VariacionesProducto;

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
    @JoinColumn(name = "id_Carrito", nullable = false)
    @JsonBackReference
    private Carrito carrito;

    @ManyToOne
    @JoinColumn(name = "id_Variacion", nullable = false)
    private VariacionesProducto variacion;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Double precio;
}
