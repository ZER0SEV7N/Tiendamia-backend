package com.spring.team1.tiendamia.Models.Productos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "variaciones_producto")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Variaciones_Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Productos producto;

    private String codigo_inventario;
    private Double precio;
    private Integer stock;
    private String imagen_url;
}
