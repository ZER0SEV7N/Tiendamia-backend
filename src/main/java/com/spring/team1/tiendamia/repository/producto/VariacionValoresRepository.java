package com.spring.team1.tiendamia.repository.producto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.team1.tiendamia.models.payload.Variacion_Valores_Id;
import com.spring.team1.tiendamia.models.productos.VariacionValores;


@Repository
public interface VariacionValoresRepository extends JpaRepository<VariacionValores, Variacion_Valores_Id> {
    
}
