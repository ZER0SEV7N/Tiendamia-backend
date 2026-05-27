package com.spring.team1.tiendamia.models.usuario;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "distrito")
@Data
public class Distrito {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_Provincia")
    private Provincia provincia;

    private String nombre;


}
