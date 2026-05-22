package com.spring.team1.tiendamia.Models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "orden_detalles")
@Data
public class Orden_Detalles {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "orden_id")
    private Ordenes orden;

    @ManyToOne
    @JoinColumn(name = "variacion_id")
    private Variaciones_Producto variacion;

    private Integer cantidad;
    private Double precio_unitario;
}
