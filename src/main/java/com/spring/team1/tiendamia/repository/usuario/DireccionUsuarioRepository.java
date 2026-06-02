package com.spring.team1.tiendamia.repository.usuario;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.team1.tiendamia.models.usuario.ubicacion.DireccionesUsuarios;

@Repository
public interface DireccionUsuarioRepository extends JpaRepository<DireccionesUsuarios, Integer> {

    List<DireccionesUsuarios> findByUsuarioId(Integer usuarioId);

    
}
