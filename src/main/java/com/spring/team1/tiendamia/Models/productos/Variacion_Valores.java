package com.spring.team1.tiendamia.Models.Productos;

import com.spring.team1.tiendamia.Models.payload.Variacion_Valores_Id;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "variacion_valores")
@Data
@NoArgsConstructor
public class Variacion_Valores {
    @EmbeddedId
    private Variacion_Valores_Id id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("variacionId") // Enlaza con el campo del ID compuesto
    @JoinColumn(name = "variacion_id")
    private Variaciones_Producto variacion;

    // Relación con el valor del filtro (Color, RAM, etc.)
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("valorAtributoId") // Enlaza con el campo del ID compuesto
    @JoinColumn(name = "valor_atributo_id")
    private Valores_Atributos valorAtributo;

    public Variacion_Valores(Variaciones_Producto variacion, Valores_Atributos valorAtributo) {
        this.variacion = variacion;
        this.valorAtributo = valorAtributo;
        // Seteamos los IDs manuales para la llave compuesta
        this.id = new Variacion_Valores_Id(variacion.getId(), valorAtributo.getId());
    }
}
