package com.spring.team1.tiendamia.models.productos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "variaciones_producto")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VariacionesProducto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Productos producto;

    @Column(name = "codigo_inventario")
    private String codigo_inventario;

    @Column(name = "precio")
    private Double precio;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "imagen_url")
    private String imagenUrl;
}
