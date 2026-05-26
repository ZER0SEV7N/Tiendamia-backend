package com.spring.team1.tiendamia.services.categoria;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.team1.tiendamia.Models.payload.categoria.CategoriaRequestDTO;
import com.spring.team1.tiendamia.Models.payload.categoria.ListCategorias;
import com.spring.team1.tiendamia.Models.productos.Categorias;
import com.spring.team1.tiendamia.repository.categoria.CategoriaRepository;

@Service
public class CategoriaService {
    @Autowired CategoriaRepository categoriaRepository;

    public List<Categorias> getAllCategorias() {
        return categoriaRepository.findAll();
    }

    public List<ListCategorias> getAllCategoriasPadre() {
        return categoriaRepository.findByCategoriasPadre().stream().map(categoria -> {
            ListCategorias dto = new ListCategorias();
            dto.setId(categoria.getId());
            dto.setNombre(categoria.getNombre());
            return dto;
        }).toList();
    }

    public List<ListCategorias> getAllCategoriasHija() {
        return categoriaRepository.findCategoriasHija().stream().map(categoria -> {
            ListCategorias dto = new ListCategorias();
            dto.setId(categoria.getId());
            dto.setNombre(categoria.getNombre());
            return dto;
        }).toList();
    }

    public Categorias getCategoriaById(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }

    public String createCategoria(CategoriaRequestDTO dto) {
        if (categoriaRepository.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new IllegalStateException("Ya existe una categoría con ese nombre");
        }

        Categorias categoria = new Categorias();
        categoria.setNombre(dto.getNombre());
        categoria.setSlug(dto.getSlug());
        if (dto.getId_categoria_padre() != null) {
            Categorias categoriaPadre = categoriaRepository.findById(dto.getId_categoria_padre())
                    .orElseThrow(() -> new RuntimeException("Categoría padre no encontrada"));
            categoria.setCategoriaPadre(categoriaPadre);
        }

        categoriaRepository.save(categoria);
        return "Categoría creada correctamente";
    }

    public String updateCategoria(Long id, CategoriaRequestDTO dto) {
        Categorias categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        if (!categoria.getNombre().equalsIgnoreCase(dto.getNombre()) &&
            categoriaRepository.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new IllegalStateException("Ya existe una categoría con ese nombre");
        }

        categoria.setNombre(dto.getNombre());
        categoria.setSlug(dto.getSlug());
        if (dto.getId_categoria_padre() != null) {
            Categorias categoriaPadre = categoriaRepository.findById(dto.getId_categoria_padre())
                    .orElseThrow(() -> new RuntimeException("Categoría padre no encontrada"));
            categoria.setCategoriaPadre(categoriaPadre);
        } else {
            categoria.setCategoriaPadre(null);
        }

        categoriaRepository.save(categoria);
        return "Categoría actualizada correctamente";
    }
}
