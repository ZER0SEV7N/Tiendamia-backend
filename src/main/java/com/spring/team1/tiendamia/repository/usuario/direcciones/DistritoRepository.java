package com.spring.team1.tiendamia.repository.usuario.direcciones;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.team1.tiendamia.models.usuario.ubicacion.Distrito;

@Repository
public interface DistritoRepository extends JpaRepository<Distrito, Integer> {
	java.util.Optional<Distrito> findByNombreIgnoreCase(String nombre);
}