package com.spring.team1.tiendamia.repository.producto;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.team1.tiendamia.Models.productos.Valores_Atributos;

@Repository
public interface ValoresAtributosRepository extends JpaRepository<Valores_Atributos, Long> {
    Optional<Valores_Atributos> findByValorIgnoreCaseAndAtributoId(String valor, Long atributoId);
}
