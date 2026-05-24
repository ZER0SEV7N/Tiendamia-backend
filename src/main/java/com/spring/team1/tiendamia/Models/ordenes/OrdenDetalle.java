package com.spring.team1.tiendamia.Models.Ordenes;

import com.spring.team1.tiendamia.Models.Productos.Variaciones_Producto;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "orden_detalles")
@Data
public class OrdenDetalle {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "orden_id")
    private Ordenes orden;

    @ManyToOne
    @JoinColumn(name = "variacion_id")
    private Variaciones_Producto variacion;

    private Integer cantidad;
    private Double precio_unitario;
}
