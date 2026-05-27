package com.spring.team1.tiendamia.repository.marca;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.team1.tiendamia.models.productos.Marcas;

@Repository
public interface MarcaRepository extends JpaRepository<Marcas, Integer> {
    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Integer id);
}
