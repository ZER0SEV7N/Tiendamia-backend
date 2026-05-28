package com.spring.team1.tiendamia.repository.categoria;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.team1.tiendamia.models.productos.Categorias;

@Repository
public interface CategoriaRepository extends JpaRepository<Categorias, Integer> {
    boolean existsByNombreIgnoreCase(String nombre);

    List<Categorias> findByCategoriaPadreIsNull();

    @Query("SELECT c FROM Categorias c WHERE c.categoriaPadre IS NOT NULL AND c.categoriaPadre.categoriaPadre IS NULL")
    List<Categorias> findCategoriasHijasPuras();

    @Query("SELECT c FROM Categorias c WHERE c.categoriaPadre IS NOT NULL AND c.categoriaPadre.categoriaPadre IS NOT NULL")
    List<Categorias> findCategoriasNietasPuras();

}
