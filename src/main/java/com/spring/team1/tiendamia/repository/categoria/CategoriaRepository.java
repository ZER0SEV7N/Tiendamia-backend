package com.spring.team1.tiendamia.repository.categoria;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.team1.tiendamia.Models.productos.Categorias;

@Repository
public interface CategoriaRepository extends JpaRepository<Categorias, Long> {
    boolean existsByNombreIgnoreCase(String nombre);

    List<Categorias> findByCategoriaPadreIsNull(); 

    @Query("SELECT c FROM Categorias c WHERE NOT EXISTS " +
           "(SELECT h FROM Categorias h WHERE h.categoriaPadre.id = c.id)")
    List<Categorias> findByCategoriasHijas();
}
