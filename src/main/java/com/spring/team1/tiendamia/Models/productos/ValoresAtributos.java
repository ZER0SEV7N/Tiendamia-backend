package com.spring.team1.tiendamia.Models.Productos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "valores_atributos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValoresAtributos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "atributo_id")
    private Atributos atributo;

    private String valor;
}
