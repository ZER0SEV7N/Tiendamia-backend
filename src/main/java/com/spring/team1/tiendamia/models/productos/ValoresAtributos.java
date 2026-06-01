package com.spring.team1.tiendamia.models.productos;

import com.spring.team1.tiendamia.models.productos.carateristicas.Atributos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "valores_atributo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValoresAtributos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_Atributo")
    private Atributos atributo;

    private String valor;
}
