package com.spring.team1.tiendamia.models.productos;

import com.spring.team1.tiendamia.models.payload.Variacion_Valores_Id;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "variacion_valores")
@Data
@NoArgsConstructor
public class VariacionValores {
    @EmbeddedId
    private Variacion_Valores_Id id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("variacionId") // Enlaza con el campo del ID compuesto
    @JoinColumn(name = "variacion_id")
    private VariacionesProducto variacion;

    // Relación con el valor del filtro (Color, RAM, etc.)
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("valorAtributoId") // Enlaza con el campo del ID compuesto
    @JoinColumn(name = "valor_atributo_id")
    private ValoresAtributos valorAtributo;

    public VariacionValores(VariacionesProducto variacion, ValoresAtributos valorAtributo) {
        this.variacion = variacion;
        this.valorAtributo = valorAtributo;
        // Seteamos los IDs manuales para la llave compuesta
        this.id = new Variacion_Valores_Id(variacion.getId(), valorAtributo.getId());
    }
}
