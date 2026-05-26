package com.spring.team1.tiendamia.models.productos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "valores_atributos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Valores_Atributos {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "atributo_id")
    private Atributos atributo;

    private String valor;
}
