package com.spring.team1.tiendamia.repository.usuario;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.team1.tiendamia.Models.Usuario.Direcciones_usuarios;

@Repository
public interface DireccionUsuarioRepository extends JpaRepository<Direcciones_usuarios, Integer> {

    List<Direcciones_usuarios> findByUsuarioId(Integer usuarioId);

    
}
