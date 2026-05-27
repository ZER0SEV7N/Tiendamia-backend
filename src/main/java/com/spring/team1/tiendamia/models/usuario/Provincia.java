package com.spring.team1.tiendamia.models.usuario;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "provincia")
@Data
public class Provincia {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_Departamento")
    private Departamento departamento;

    private String nombre;
}
