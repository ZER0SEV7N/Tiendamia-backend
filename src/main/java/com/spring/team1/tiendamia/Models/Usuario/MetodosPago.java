package com.spring.team1.tiendamia.Models.Usuario;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "metodos_pago")
@Data
public class MetodosPago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuarios usuario;

    @Column(name = "pasarela")
    private String pasarela;

    @Column(name = "customer_id")
    private String customer_id;

    @Column(name = "ultimos_cuatro")
    private String ultimos_cuatro;

    @ManyToOne
    @JoinColumn(name = "id_marca_tarjeta")
    private MarcasTarjeta marca_tarjeta;

}