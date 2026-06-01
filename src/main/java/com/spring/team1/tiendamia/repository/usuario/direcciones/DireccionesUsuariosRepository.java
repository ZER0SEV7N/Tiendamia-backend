package com.spring.team1.tiendamia.repository.usuario.direcciones;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.team1.tiendamia.models.usuario.ubicacion.DireccionesUsuarios;

@Repository
public interface DireccionesUsuariosRepository extends JpaRepository<DireccionesUsuarios, Integer> {

}
