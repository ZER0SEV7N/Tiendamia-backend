package com.spring.team1.tiendamia.repository.producto.variaciones;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.team1.tiendamia.models.productos.carateristicas.Atributos;

@Repository
public interface AtributosRepository extends JpaRepository<Atributos, Integer> {
    Optional<Atributos> findByNombreIgnoreCase(String nombre);
}
