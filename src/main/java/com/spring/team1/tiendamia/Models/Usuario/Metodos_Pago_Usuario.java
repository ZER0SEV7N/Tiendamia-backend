package com.spring.team1.tiendamia.Models.Usuario;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "metodos_pago_usuario")
@Data
public class Metodos_Pago_Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuarios usuario;

    private String pasarela;
    private String customer_id;
    private String ultimos_cuatro;

    @ManyToOne
    @JoinColumn(name = "id_marca_tarjeta")
    private Marcas_Tarjeta marca_tarjeta;

    
}