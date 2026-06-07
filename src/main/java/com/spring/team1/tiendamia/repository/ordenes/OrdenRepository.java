package com.spring.team1.tiendamia.repository.ordenes;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.team1.tiendamia.models.ordenes.Ordenes;

@Repository
public interface OrdenRepository extends JpaRepository<Ordenes, Integer> {
    
    List<Ordenes> findByUsuarioIdOrderByIdDesc(Integer usuarioId);
}
