package com.spring.team1.tiendamia.repository.producto.variaciones;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.team1.tiendamia.models.productos.ValoresAtributos;


@Repository
public interface ValoresAtributosRepository extends JpaRepository<ValoresAtributos, Integer> {
    Optional<ValoresAtributos> findByValorIgnoreCaseAndAtributoId(String valor, Integer atributoId);
}
