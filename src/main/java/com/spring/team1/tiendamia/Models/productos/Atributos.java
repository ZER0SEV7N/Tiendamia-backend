package com.spring.team1.tiendamia.models.productos;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "atributos")
@Data
public class Atributos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
}
