package com.spring.team1.tiendamia.models.payload;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Variacion_Valores_Id {
    @Column(name = "variacion_id")
    private Integer variacionId;

    @Column(name = "valor_atributo_id")
    private Integer valorAtributoId;
    
}

