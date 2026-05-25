package com.spring.team1.tiendamia.Models.Productos;

import jakarta.persistence.*;
import lombok.Data;


//xd
@Entity
@Table(name = "atributos")
@Data
public class Atributos {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
}
