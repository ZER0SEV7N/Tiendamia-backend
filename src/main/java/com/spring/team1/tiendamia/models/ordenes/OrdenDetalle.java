package com.spring.team1.tiendamia.models.ordenes;

import com.spring.team1.tiendamia.models.productos.VariacionesProducto;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "orden_detalle")
@Data
public class OrdenDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_Orden")
    private Ordenes orden;

    @ManyToOne
    @JoinColumn(name = "id_Variacion")
    private VariacionesProducto variacion;

    private Integer cantidad;
    private Double precio_unitario;
}
