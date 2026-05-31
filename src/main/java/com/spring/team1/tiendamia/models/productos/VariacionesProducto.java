package com.spring.team1.tiendamia.models.productos;

import java.util.List;

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
    @JoinColumn(name = "id_Producto")
    private Productos producto;

    @Column(name = "codigo_inventario")
    private String codigoInventario;

    @Column(name = "precio")
    private Double precio;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "imagen_url")
    private String imagenUrl;

    @OneToMany(mappedBy = "variacion", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<VariacionValores> variacionValores;
}
