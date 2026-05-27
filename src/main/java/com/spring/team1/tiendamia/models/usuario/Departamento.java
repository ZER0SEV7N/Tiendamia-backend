package com.spring.team1.tiendamia.models.usuario;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "departamento")
@Data
public class Departamento {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
}
