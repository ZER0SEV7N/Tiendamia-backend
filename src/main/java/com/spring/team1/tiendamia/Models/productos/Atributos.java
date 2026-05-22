package com.spring.team1.tiendamia.Models.productos;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "atributos")
@Data
public class Atributos {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
}
